/**
 * The BSD 3-Clause License ("BSD New" or "BSD Simplified")
 *
 * Copyright (c) 2015, Carl-Heinz Genzel
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER
 * OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.hsbremen.tc.tnc.im.adapter.connection;

import java.util.LinkedList;
import java.util.List;

import org.ietf.nea.pa.attribute.PaAttribute;
import org.ietf.nea.pa.message.PaMessageFactoryIetf;
import org.trustedcomputinggroup.tnc.ifimv.IMVConnection;
import org.trustedcomputinggroup.tnc.ifimv.IMVConnectionLong;
import org.trustedcomputinggroup.tnc.ifimv.TNCConstants;
import org.trustedcomputinggroup.tnc.ifimv.TNCException;

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
import de.hsbremen.tc.tnc.report.ImvRecommendationPair;
import de.hsbremen.tc.tnc.report.enums.ImHandshakeRetryReasonEnum;

/**
 * Connection adapter for an IMV connection according to IETF/TCG
 * specifications.
 *
 */
class ImvConnectionAdapterIetf implements ImvConnectionAdapter {

    private final ImWriter<ImMessage> byteWriter;
    private final IMVConnection connection;

    /**
     * Creates a connection adapter with the given message writer for the
     * specified IMV connection.
     *
     * @param byteWriter the message writer
     * @param connection the connection to adapt
     */
    @SuppressWarnings("unchecked")
    ImvConnectionAdapterIetf(final ImWriter<? extends ImMessage> byteWriter,
            final IMVConnection connection) {
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
        if (reason.toString().contains("IMV")) {
            try {
                this.connection.requestHandshakeRetry(reason.id());
            } catch (TNCException e) {
                throw new TncException(e);
            }
        } else {
            throw new TncException(
                    "Reason is not useable with IMV and IMVConnection.",
                    TncExceptionCodeEnum.TNC_RESULT_INVALID_PARAMETER);
        }
    }

    @Override
    public void provideRecommendation(final ImvRecommendationPair pair)
            throws TncException {

        try {
            this.connection.provideRecommendation(pair.getRecommendationId(),
                    pair.getResultId());

        } catch (TNCException e) {
            throw new TncException(e);
        }
    }

    @Override
    public Object getAttribute(final TncAttributeType type)
            throws TncException {
        try {
            return this.connection.getAttribute(type.id());
        } catch (TNCException e) {
            throw new TncException(e);
        } catch (UnsupportedOperationException e1) {
            throw new UnsupportedOperationException("The connection "
                    + this.connection.toString() + " of type "
                    + this.connection.getClass().getCanonicalName()
                    + " does not support attributes.", e1);
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

        // FIXME it maybe better to check the IMV type here too.
        if (this.connection instanceof IMVConnectionLong
                && validatorId != HSBConstants.HSB_IM_ID_UNKNOWN) {

            ((IMVConnectionLong) this.connection).sendMessageLong(flags,
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
     * Removes any message attribute which is not of type PaAttribute, because
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
