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
package de.hsbremen.tc.tnc.tnccs.adapter.im;

import org.ietf.nea.pb.message.PbMessageValueIm;
import org.ietf.nea.pb.message.enums.PbMessageImFlagsEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trustedcomputinggroup.tnc.ifimc.AttributeSupport;
import org.trustedcomputinggroup.tnc.ifimc.IMC;
import org.trustedcomputinggroup.tnc.ifimc.IMCLong;
import org.trustedcomputinggroup.tnc.ifimc.TNCConstants;
import org.trustedcomputinggroup.tnc.ifimc.TNCException;

import de.hsbremen.tc.tnc.attribute.TncAttributeType;
import de.hsbremen.tc.tnc.connection.TncConnectionState;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.exception.enums.TncExceptionCodeEnum;
import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessageValue;
import de.hsbremen.tc.tnc.tnccs.adapter.connection.ImcConnectionAdapter;
import de.hsbremen.tc.tnc.tnccs.adapter.im.exception.TerminatedException;

/**
 * IMC adapter adapting an IMC according to IETF/TCG specifications.
 *
 * @author Carl-Heinz Genzel
 *
 */
class ImcAdapterIetf implements ImcAdapter {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(ImcAdapterIetf.class);
    private IMC imc;
    private final long primaryId;

    /**
     * Creates an IMC adapter for the given IMC
     * with the given primary ID.
     *
     * @param imc the IMC to adapt
     * @param primaryId the primary ID
     */
    ImcAdapterIetf(final IMC imc, final long primaryId) {
        this.imc = imc;
        this.primaryId = primaryId;
    }

    @Override
    public long getPrimaryId() {
        return this.primaryId;
    }

    @Override
    public void notifyConnectionChange(final ImcConnectionAdapter connection,
            final TncConnectionState state)
            throws TncException, TerminatedException {
        try {
            this.imc.notifyConnectionChange(connection, state.id());
        } catch (TNCException e) {
            throw new TncException(e);
        } catch (NullPointerException e) {
            throw new TerminatedException("The IMC with ID " + this.primaryId
                    + " is terminated and should be removed.", e, new Long(
                    this.primaryId));
        }
    }

    @Override
    public void beginHandshake(final ImcConnectionAdapter connection)
            throws TncException, TerminatedException {
        try {
            connection.allowMessageReceipt();
            this.imc.beginHandshake(connection);
        } catch (TNCException e) {
            throw new TncException(e);
        } catch (NullPointerException e) {
            throw new TerminatedException("The IMC with ID " + this.primaryId
                    + " is terminated and should be removed.", e, new Long(
                    this.primaryId));
        } finally {
            connection.denyMessageReceipt();
        }
    }

    @Override
    public void handleMessage(final ImcConnectionAdapter connection,
            final TnccsMessageValue message)
            throws TncException, TerminatedException {
        try {
            this.dispatchMessageToImc(connection, message);
        } catch (NullPointerException e) {
            throw new TerminatedException("The IMC with ID " + this.primaryId
                    + " is terminated and should be removed.", e, new Long(
                    this.primaryId));
        }
    }

    @Override
    public void batchEnding(final ImcConnectionAdapter connection)
            throws TncException, TerminatedException {

        try {
            connection.allowMessageReceipt();
            this.imc.batchEnding(connection);
        } catch (TNCException e) {
            throw new TncException(e);
        } catch (NullPointerException e) {
            throw new TerminatedException("The IMC with ID " + this.primaryId
                    + " is terminated and should be removed.", e, new Long(
                    this.primaryId));
        } finally {
            connection.denyMessageReceipt();
        }
    }

    @Override
    public Object getAttribute(final TncAttributeType type)
            throws TncException {

        if (this.imc != null && this.imc instanceof AttributeSupport) {
            try {
                return ((AttributeSupport) this.imc).getAttribute(type.id());
            } catch (TNCException e) {
                throw new TncException(e);
            } catch (NullPointerException e) {
                throw new UnsupportedOperationException(e.getMessage());
            }
        } else {
            throw new UnsupportedOperationException(
                    "The underlying IMC is not of type "
                            + AttributeSupport.class.getCanonicalName() + ".");
        }
    }

    @Override
    public void setAttribute(final TncAttributeType type, final Object value)
            throws TncException {

        if (this.imc != null && this.imc instanceof AttributeSupport) {
            try {
                ((AttributeSupport) this.imc).setAttribute(type.id(), value);
            } catch (TNCException e) {
                throw new TncException(e);
            } catch (NullPointerException e) {
                throw new UnsupportedOperationException(e.getMessage());
            }
        } else {
            throw new UnsupportedOperationException(
                    "The underlying IMC is not of type "
                            + AttributeSupport.class.getCanonicalName() + ".");
        }
    }

    @Override
    public void terminate() throws TerminatedException {

        try {
            this.imc.terminate();
        } catch (TNCException e) {
            LOGGER.warn("An error occured, while terminating IMC/V " + this.imc
                    + ". IMC/V will be removed anyway.", e);
        } catch (NullPointerException e) {
            throw new TerminatedException("The IMC with ID " + this.primaryId
                    + " is terminated and should be removed.", e, new Long(
                    this.primaryId));
        }

        this.imc = null;
    }

    /**
     * Dispatches a message from a given connection to an IMV according
     * to its message reception abilities.
     *
     * @param connection the connection
     * @param value the message
     * @throws TncException if message dispatch fails
     */
    private void dispatchMessageToImc(final ImcConnectionAdapter connection,
            final TnccsMessageValue value) throws TncException {

        if (value instanceof PbMessageValueIm) {
            PbMessageValueIm pbValue = (PbMessageValueIm) value;

            if (this.imc instanceof IMCLong) {
                pbValue.getImFlags();

                byte bFlags = 0;
                for (PbMessageImFlagsEnum pbMessageImFlagsEnum : pbValue
                        .getImFlags()) {
                    bFlags |= pbMessageImFlagsEnum.bit();
                }
                try {
                    connection.allowMessageReceipt();
                    ((IMCLong) this.imc).receiveMessageLong(connection, bFlags,
                            pbValue.getSubVendorId(), pbValue.getSubType(),
                            pbValue.getMessage(), pbValue.getValidatorId(),
                            pbValue.getCollectorId());
                } catch (TNCException e) {
                    throw new TncException(e);
                } finally {
                    connection.denyMessageReceipt();
                }
            } else {
                if (pbValue.getSubType() <= TNCConstants.TNC_SUBTYPE_ANY) {

                    final int vendorIdShift = 8;
                    final int messageTypeMask = 0xFF;

                    long msgType =
                            (long) (pbValue.getSubVendorId() << vendorIdShift)
                            | (pbValue.getSubType() & messageTypeMask);
                    try {
                        connection.allowMessageReceipt();
                        this.imc.receiveMessage(connection, msgType,
                                pbValue.getMessage());
                    } catch (TNCException e) {
                        throw new TncException(e);
                    } finally {
                        connection.denyMessageReceipt();
                    }
                } else {
                    LOGGER.warn(new StringBuilder()
                    .append("The IMC does not support message types ")
                    .append("greater than ")
                    .append(TNCConstants.TNC_SUBTYPE_ANY)
                    .append(". The message with type ")
                    .append(pbValue.getSubType())
                    .append(" will be ignored.").toString());
                }
            }
        } else {
            throw new TncException("Message values of type other than "
                    + PbMessageValueIm.class.getCanonicalName()
                    + " are not supported.",
                    TncExceptionCodeEnum.TNC_RESULT_INVALID_PARAMETER,
                    PbMessageValueIm.class.getCanonicalName());
        }
    }
}
