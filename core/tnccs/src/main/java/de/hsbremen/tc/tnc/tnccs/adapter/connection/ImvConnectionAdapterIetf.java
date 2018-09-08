/**
 * The BSD 3-Clause License ("BSD New" or "BSD Simplified")
 *
 * Copyright © 2015 Trust HS Bremen and its Contributors. All rights   
 * reserved.
 *
 * See the CONTRIBUTORS file distributed with this work for additional
 * information regarding copyright ownership.
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
package de.hsbremen.tc.tnc.tnccs.adapter.connection;

import org.ietf.nea.pb.message.PbMessageFactoryIetf;
import org.ietf.nea.pb.message.enums.PbMessageImFlagEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trustedcomputinggroup.tnc.ifimv.TNCConstants;
import org.trustedcomputinggroup.tnc.ifimv.TNCException;

import de.hsbremen.tc.tnc.HSBConstants;
import de.hsbremen.tc.tnc.attribute.DefaultTncAttributeTypeFactory;
import de.hsbremen.tc.tnc.attribute.TncCommonAttributeTypeEnum;
import de.hsbremen.tc.tnc.attribute.TncHsbAttributeTypeEnum;
import de.hsbremen.tc.tnc.attribute.TncServerAttributeTypeEnum;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessage;
import de.hsbremen.tc.tnc.report.ImvRecommendationPair;
import de.hsbremen.tc.tnc.report.ImvRecommendationPairFactory;
import de.hsbremen.tc.tnc.report.enums.HandshakeRetryReasonEnum;

/**
 * IMV connection adapter adapting a simple IMV connection according to
 * IETF/TCG specifications.
 *
 *
 */
class ImvConnectionAdapterIetf extends AbstractImConnectionAdapter implements
        ImvConnectionAdapter {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(ImvConnectionAdapterIetf.class);

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
            m = PbMessageFactoryIetf.createIm(new PbMessageImFlagEnum[0],
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
            this.context.requestHandshakeRetry(HandshakeRetryReasonEnum
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

        if (LOGGER.isDebugEnabled()) {
            StringBuilder b = new StringBuilder();
            b.append("IMV # ")
            .append(super.getImId())
            .append(" recommends ")
            .append(recommendationPair.getRecommendation().toString())
            .append(" access for the result ")
            .append(recommendationPair.getResult().toString());

            try {
                Object connectionId = this.context.getAttribute(
                        TncHsbAttributeTypeEnum.HSB_ATTRIBUTEID_IFT_ID);
                if (connectionId instanceof String) {
                    b.append(" for the connection with ID ")
                    .append((String) connectionId);
                }
            } catch (TncException e) {
               LOGGER.debug("Could not identify connection "
                       + "via connection context.");
            }
            b.append(".");
            LOGGER.debug(b.toString());
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
