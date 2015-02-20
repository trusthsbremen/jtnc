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
package de.hsbremen.tc.tnc.tnccs.adapter.connection;

import org.ietf.nea.pb.message.PbMessageFactoryIetf;
import org.ietf.nea.pb.message.enums.PbMessageImFlagsEnum;
import org.trustedcomputinggroup.tnc.ifimv.TNCConstants;
import org.trustedcomputinggroup.tnc.ifimv.TNCException;

import de.hsbremen.tc.tnc.HSBConstants;
import de.hsbremen.tc.tnc.attribute.DefaultTncAttributeTypeFactory;
import de.hsbremen.tc.tnc.attribute.TncCommonAttributeTypeEnum;
import de.hsbremen.tc.tnc.attribute.TncServerAttributeTypeEnum;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessage;
import de.hsbremen.tc.tnc.report.ImvRecommendationPair;
import de.hsbremen.tc.tnc.report.ImvRecommendationPairFactory;
import de.hsbremen.tc.tnc.report.enums.ImHandshakeRetryReasonEnum;

/**
 * IMV connection adapter adapting a simple IMV connection according to
 * IETF/TCG specifications.
 *
 * @author Carl-Heinz Genzel
 *
 */
class ImvConnectionAdapterIetf extends AbstractImConnectionAdapter implements
        ImvConnectionAdapter {

    private final ImvConnectionContext context;
    private long maxMessageSize;

    /**
     * Creates an IMV connection adapter for an IMV with the
     * given ID and the given IMV connection context.
     *
     * @param primaryImvId the IMV ID
     * @param context the connection context
     */
    ImvConnectionAdapterIetf(final int primaryImvId,
            final ImvConnectionContext context) {
        super(primaryImvId);
        this.context = context;

        try {
            Object o = context.getAttribute(TncCommonAttributeTypeEnum
                    .TNC_ATTRIBUTEID_MAX_MESSAGE_SIZE);
            if (o instanceof Long) {
                this.maxMessageSize = ((Long) o).longValue();
            }
        } catch (TncException | UnsupportedOperationException e) {
            this.maxMessageSize = HSBConstants.TCG_IM_MAX_MESSAGE_SIZE_UNKNOWN;
        }

    }

    @Override
    public void sendMessage(final long messageType, final byte[] message)
            throws TNCException {

        final int vendorIdShift = 8;
        final int messageTypeMask = 0xFF;

        if (messageType
                == (TNCConstants.TNC_VENDORID_ANY << vendorIdShift
                        | TNCConstants.TNC_SUBTYPE_ANY)) {
            throw new TNCException("Message type is set to reserved type.",
                    TNCException.TNC_RESULT_INVALID_PARAMETER);
        }

        if (!this.isReceiving()) {
            throw new TNCException(
                    "Connection is currently not allowed to receive messages.",
                    TNCException.TNC_RESULT_ILLEGAL_OPERATION);
        }

        this.checkMessageSize(message.length);

        long vendorId = messageType >>> vendorIdShift;
        long type = messageType & messageTypeMask;

        TnccsMessage m = null;
        try {
            m = PbMessageFactoryIetf.createIm(new PbMessageImFlagsEnum[0],
                    vendorId, type, (int) TNCConstants.TNC_IMCID_ANY,
                    (int) super.getImId(), message);
        } catch (ValidationException e) {
            throw new TNCException(e.getCause().getMessage(), e.getCause()
                    .getErrorCode());
        }

        try {
            this.sendMessage(m);
        } catch (TncException e) {
            throw new TNCException(e.getMessage(), e.getResultCode().id());
        }
    }

    @Override
    public void requestHandshakeRetry(final long reason) throws TNCException {
        try {
            this.context.requestHandshakeRetry(ImHandshakeRetryReasonEnum
                    .fromId(reason));
        } catch (TncException e) {
            throw new TNCException(e.getMessage(), e.getResultCode().id());
        }

    }

    @Override
    public Object getAttribute(final long attributeID) throws TNCException {

        if (attributeID == TncServerAttributeTypeEnum
                .TNC_ATTRIBUTEID_PRIMARY_IMV_ID.id()) {

            return new Long(super.getImId());
        } else {
            try {

                return this.context.getAttribute(DefaultTncAttributeTypeFactory
                        .getInstance().fromId(attributeID));
            } catch (TncException e) {
                throw new TNCException(e.getMessage(), e.getResultCode().id());
            }
        }
    }

    @Override
    public void setAttribute(final long attributeID,
            final Object attributeValue) throws TNCException {
        throw new UnsupportedOperationException(
                "The operation setAttribute(...) is not supported, "
                + "because there are no attributes to set.");
    }

    @Override
    public void provideRecommendation(final long recommendation,
            final long evaluation) throws TNCException {
        if (!this.isReceiving()) {
            throw new TNCException(
                    "Connection is currently not allowed to receive messages.",
                    TNCException.TNC_RESULT_ILLEGAL_OPERATION);
        }

        ImvRecommendationPair recommendationPair = null;
        try {
            recommendationPair = ImvRecommendationPairFactory
                    .createRecommendationPair(recommendation, evaluation);
        } catch (IllegalArgumentException e) {
            throw new TNCException(e.getMessage(),
                    TNCException.TNC_RESULT_INVALID_PARAMETER);
        }

        try {
            this.context.addRecommendation(super.getImId(), recommendationPair);
        } catch (TncException e) {
            throw new TNCException(e.getMessage(), e.getResultCode().id());
        }
    }

    /**
     * Sends a message to the context for transmission.
     *
     * @param message the message to transmit
     * @throws TncException if message cannot be send to the context
     */
    protected final void sendMessage(final TnccsMessage message)
            throws TncException {
        if (message != null) {
            this.context.addMessage(message);
        }
    }

    /**
     * Checks if message size exceeds the maximum message size, that is
     * supported for the connection.
     *
     * @param size  the message size
     * @throws TNCException if message size exceeds maximum message size
     */
    protected void checkMessageSize(final int size) throws TNCException {
        if (this.maxMessageSize != HSBConstants
                    .TCG_IM_MAX_MESSAGE_SIZE_UNKNOWN
                && this.maxMessageSize != HSBConstants
                    .TCG_IM_MAX_MESSAGE_SIZE_UNLIMITED) {

            if (size > this.maxMessageSize) {

                throw new TNCException("Maximum message size of "
                        + this.maxMessageSize + " exceeded.",
                        TNCException.TNC_RESULT_EXCEEDED_MAX_MESSAGE_SIZE);
            }
        }
    }
}
