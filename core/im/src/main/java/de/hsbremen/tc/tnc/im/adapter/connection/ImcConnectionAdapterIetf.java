/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Carl-Heinz Genzel
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 */
package de.hsbremen.tc.tnc.im.adapter.connection;

import java.util.LinkedList;
import java.util.List;

import org.ietf.nea.pa.attribute.PaAttribute;
import org.ietf.nea.pa.message.PaMessageFactoryIetf;
import org.trustedcomputinggroup.tnc.ifimc.AttributeSupport;
import org.trustedcomputinggroup.tnc.ifimc.IMCConnection;
import org.trustedcomputinggroup.tnc.ifimc.IMCConnectionLong;
import org.trustedcomputinggroup.tnc.ifimc.TNCConstants;
import org.trustedcomputinggroup.tnc.ifimc.TNCException;

import de.hsbremen.tc.tnc.HSBConstants;
import de.hsbremen.tc.tnc.attribute.TncAttributeType;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.exception.enums.TncExceptionCodeEnum;
import de.hsbremen.tc.tnc.im.adapter.data.ImObjectComponent;
import de.hsbremen.tc.tnc.im.adapter.data.enums.PaComponentFlagsEnum;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.attribute.ImAttribute;
import de.hsbremen.tc.tnc.message.m.message.ImMessage;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.message.util.DefaultByteBuffer;
import de.hsbremen.tc.tnc.report.enums.ImHandshakeRetryReasonEnum;

/**
 * Connection adapter for a IMC connection according to IETF/TCG
 * specifications.
 *
 */
class ImcConnectionAdapterIetf implements ImcConnectionAdapter {

    private final ImWriter<ImMessage> byteWriter;
    private final IMCConnection connection;

    /**
     * Creates a connection adapter with the given message writer for the
     * specified IMC connection.
     *
     * @param byteWriter the message writer
     * @param connection the connection to adapt
     */
    @SuppressWarnings("unchecked")
    ImcConnectionAdapterIetf(final ImWriter<? extends ImMessage> byteWriter,
            final IMCConnection connection) {
        this.byteWriter = (ImWriter<ImMessage>) byteWriter;
        this.connection = connection;
    }

    @Override
    public void sendMessage(final ImObjectComponent component,
            final long identifier)
            throws TncException, ValidationException {

        if (component != null && component.getAttributes() != null) {

            ImMessage message = PaMessageFactoryIetf.createMessage(identifier,
                    this.filterTypes(component.getAttributes()));

            byte[] byteMessage = this.messageToByteArray(message);

            byte flags = 0;
            for (PaComponentFlagsEnum flagEnum : component.getImFlags()) {
                flags |= flagEnum.bit();
            }

            try {
                this.send(flags, component.getVendorId(), component.getType(),
                        component.getCollectorId(), component.getValidatorId(),
                        byteMessage);

            } catch (TNCException e) {
                throw new TncException(e);
            }

        } // else ignore and do nothing

    }

    @Override
    public void requestHandshakeRetry(final ImHandshakeRetryReasonEnum reason)
            throws TncException {
        if (reason.toString().contains("IMC")) {
            try {
                this.connection.requestHandshakeRetry(reason.id());
            } catch (TNCException e) {
                throw new TncException(e);
            }
        } else {
            throw new TncException(
                    "Reason is not useable with IMC and IMCConnection.",
                    TncExceptionCodeEnum.TNC_RESULT_INVALID_PARAMETER);
        }
    }

    @Override
    public Object getAttribute(final TncAttributeType type)
            throws TncException {
        if (this.connection instanceof AttributeSupport) {
            try {
                return ((AttributeSupport) this.connection)
                        .getAttribute(type.id());
            } catch (TNCException e) {
                throw new TncException(e);
            }
        } else {
            throw new UnsupportedOperationException("The connection "
                    + this.connection.toString() + " of type "
                    + this.connection.getClass().getCanonicalName()
                    + " does not support attributes.");
        }
    }

    /**
     * Sends the message for an integrity measurement component to the adapted
     * connection.
     *
     * @param flags the message flags as composed byte
     * @param vendorId the vendor ID describing the component
     * @param type the type ID describing the component
     * @param collectorId the referred IMC
     * @param validatorId the referred IMV
     * @param message the raw message
     * @throws TNCException if the message cannot be send
     */
    private void send(final byte flags,
            final long vendorId, final long type,
            final long collectorId, final long validatorId,
            final byte[] message) throws TNCException {

        // FIXME it maybe better to check the IMC type here too.
        if (this.connection instanceof IMCConnectionLong
                && collectorId != HSBConstants.HSB_IM_ID_UNKNOWN) {

            ((IMCConnectionLong) this.connection).sendMessageLong(flags,
                    vendorId, type, message, collectorId, validatorId);

        } else {
            if (type >= TNCConstants.TNC_SUBTYPE_ANY
                    || vendorId == TNCConstants.TNC_VENDORID_ANY) {
                throw new TNCException(
                        "Connection does not support the message type " + type
                                + " with vendor ID " + vendorId
                                + ", type cannot be greater than "
                                + TNCConstants.TNC_SUBTYPE_ANY
                                + " and vendor ID cannot be greater than "
                                + TNCConstants.TNC_VENDORID_ANY + ".",
                        TNCException.TNC_RESULT_NO_LONG_MESSAGE_TYPES);
            }

            final int vendorIdShift = 8;
            final int messageTypeMask = 0xFF;

            long msgType = (long) (vendorId << vendorIdShift)
                    | (type & messageTypeMask);

            this.connection.sendMessage(msgType, message);
        }

    }

    /**
     * Removes any message attribute, which is not of type PaAttribute, because
     * only these type can be send thru a IETF compatible connection.
     *
     * @param imAttributes the attributes to filter
     * @return the attributes of type PaAttribute
     */
    private List<PaAttribute> filterTypes(
            final List<? extends ImAttribute> imAttributes) {
        List<PaAttribute> attributes = new LinkedList<>();
        for (ImAttribute attr : imAttributes) {
            if (attr instanceof PaAttribute) {
                attributes.add((PaAttribute) attr);
            }
        }
        return attributes;
    }

    /**
     * Serializes a message to an array of bytes.
     *
     * @param message the message to serialize
     * @return the byte array
     * @throws TncException if a fatal exceptions is thrown while serialization
     */
    private byte[] messageToByteArray(final ImMessage message)
            throws TncException {

        ByteBuffer buf = new DefaultByteBuffer(message.getHeader().getLength());
        try {
            this.byteWriter.write(message, buf);
        } catch (SerializationException e) {
            throw new TncException(e.getMessage(),
                    TncExceptionCodeEnum.TNC_RESULT_OTHER);
        }

        return buf.read((int) (buf.bytesWritten() - buf.bytesRead()));

    }

}
