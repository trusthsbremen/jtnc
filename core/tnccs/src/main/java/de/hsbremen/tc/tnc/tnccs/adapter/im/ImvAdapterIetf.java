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
import org.ietf.nea.pb.message.enums.PbMessageImFlagEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trustedcomputinggroup.tnc.ifimv.IMV;
import org.trustedcomputinggroup.tnc.ifimv.IMVLong;
import org.trustedcomputinggroup.tnc.ifimv.IMVTNCSFirst;
import org.trustedcomputinggroup.tnc.ifimv.TNCConstants;
import org.trustedcomputinggroup.tnc.ifimv.TNCException;

import de.hsbremen.tc.tnc.connection.TncConnectionState;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.exception.enums.TncExceptionCodeEnum;
import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessageValue;
import de.hsbremen.tc.tnc.tnccs.adapter.connection.ImvConnectionAdapter;
import de.hsbremen.tc.tnc.tnccs.adapter.im.exception.TerminatedException;

/**
 * IMV adapter adapting an IMV according to IETF/TCG specifications.
 *
 *
 */
class ImvAdapterIetf implements ImvAdapter {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(ImvAdapterIetf.class);
    private IMV imv;
    private final long primaryId;

    /**
     * Creates an IMV adapter for the given IMV
     * with the given primary ID.
     *
     * @param imv the IMV to adapt
     * @param primaryId the primary ID
     */
    ImvAdapterIetf(final IMV imv, final long primaryId) {
        this.imv = imv;
        this.primaryId = primaryId;
    }

    @Override
    public long getPrimaryId() {
        return this.primaryId;
    }

    @Override
    public void notifyConnectionChange(final ImvConnectionAdapter connection,
            final TncConnectionState state)
            throws TncException, TerminatedException {
        try {
            this.imv.notifyConnectionChange(connection, state.id());
        } catch (TNCException e) {
            throw new TncException(e);
        } catch (NullPointerException e) {
            throw new TerminatedException("The IMV with ID " + this.primaryId
                    + " is terminated and should be removed.", e, new Long(
                    this.primaryId));
        }
    }

    @Override
    public void beginHandshake(final ImvConnectionAdapter connection)
            throws TncException, TerminatedException {

        try {
            if (this.imv instanceof IMVTNCSFirst) {
                connection.allowMessageReceipt();
                ((IMVTNCSFirst) this.imv).beginHandshake(connection);
            } else {
                throw new UnsupportedOperationException(
                        "The underlying IMV is not of type "
                                + IMVTNCSFirst.class.getCanonicalName() + ".");
            }
        } catch (TNCException e) {
            throw new TncException(e);
        } catch (NullPointerException e) {
            throw new TerminatedException("The IMV with ID " + this.primaryId
                    + " is terminated and should be removed.", e, new Long(
                    this.primaryId));
        } finally {
            connection.denyMessageReceipt();
        }
    }

    @Override
    public void handleMessage(final ImvConnectionAdapter connection,
            final TnccsMessageValue message)
            throws TncException, TerminatedException {
        try {
            this.dispatchMessageToImc(connection, message);
        } catch (NullPointerException e) {
            throw new TerminatedException("The IMV with ID " + this.primaryId
                    + " is terminated and should be removed.", e, new Long(
                    this.primaryId));
        }
    }

    @Override
    public void batchEnding(final ImvConnectionAdapter connection)
            throws TncException, TerminatedException {

        try {
            connection.allowMessageReceipt();
            this.imv.batchEnding(connection);

        } catch (TNCException e) {
            throw new TncException(e);
        } catch (NullPointerException e) {
            throw new TerminatedException("The IMV with ID " + this.primaryId
                    + " is terminated and should be removed.", e, new Long(
                    this.primaryId));
        } finally {
            connection.denyMessageReceipt();
        }
    }

    @Override
    public void terminate() throws TerminatedException {

        try {
            this.imv.terminate();
        } catch (TNCException e) {
            LOGGER.warn("An error occured, while terminating IMC/V " + this.imv
                    + ". IMC/V will be removed anyway.", e);
        } catch (NullPointerException e) {
            throw new TerminatedException("The IMV with ID " + this.primaryId
                    + " is terminated and should be removed.", e, new Long(
                    this.primaryId));
        }
        this.imv = null;
    }

    @Override
    public void solicitRecommendation(final ImvConnectionAdapter connection)
            throws TncException, TerminatedException {

        try {
            connection.allowMessageReceipt();
            this.imv.solicitRecommendation(connection);
        } catch (TNCException e) {
            throw new TncException(e);
        } catch (NullPointerException e) {
            throw new TerminatedException("The IMV with ID " + this.primaryId
                    + " is terminated and should be removed.", e, new Long(
                    this.primaryId));
        } finally {
            connection.denyMessageReceipt();
        }

    }

    /**
     * Dispatches a message from a given connection to an IMV according
     * to its message reception abilities.
     *
     * @param connection the connection
     * @param value the message
     * @throws TncException if message dispatch fails
     */
    private void dispatchMessageToImc(final ImvConnectionAdapter connection,
            final TnccsMessageValue value) throws TncException {

        if (value instanceof PbMessageValueIm) {
            PbMessageValueIm pbValue = (PbMessageValueIm) value;

            if (this.imv instanceof IMVLong) {
                pbValue.getImFlags();

                byte bFlags = 0;
                for (PbMessageImFlagEnum pbMessageImFlagsEnum : pbValue
                        .getImFlags()) {
                    bFlags |= pbMessageImFlagsEnum.bit();
                }
                try {
                    connection.allowMessageReceipt();
                    ((IMVLong) this.imv).receiveMessageLong(connection, bFlags,
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
                        this.imv.receiveMessage(connection, msgType,
                                pbValue.getMessage());
                    } catch (TNCException e) {
                        throw new TncException(e);
                    } finally {
                        connection.denyMessageReceipt();
                    }

                } else {
                    LOGGER.warn(new StringBuilder()
                        .append("The IMV does not support message types ")
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
