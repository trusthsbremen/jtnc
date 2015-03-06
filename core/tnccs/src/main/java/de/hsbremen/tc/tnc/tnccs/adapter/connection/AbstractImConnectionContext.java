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

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.HSBConstants;
import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.attribute.TncAttributeType;
import de.hsbremen.tc.tnc.attribute.TncCommonAttributeTypeEnum;
import de.hsbremen.tc.tnc.attribute.TncHsbAttributeTypeEnum;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.exception.enums.TncExceptionCodeEnum;
import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessage;
import de.hsbremen.tc.tnc.report.enums.ImHandshakeRetryReasonEnum;
import de.hsbremen.tc.tnc.tnccs.session.base.AttributeCollection;

/**
 * Generic base for an IM(C/V) connection context.
 *
 *
 */
public abstract class AbstractImConnectionContext implements
        ImConnectionContext {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(AbstractImConnectionContext.class);

    private final List<TnccsMessage> messageQueue;
    private final Attributed attributes;
    private final ConnectionHandshakeRetryListener listener;
    private boolean valid;
    private long maxRoundTrips;

    /**
     * Creates an IM(C/V) connection context base with the given
     * common session and/or connection attributes and the handshake
     * retry listener.
     *
     * @param attributes the common session/connection attributes
     * @param listener the handshake retry listener
     */
    protected AbstractImConnectionContext(final Attributed attributes,
            final ConnectionHandshakeRetryListener listener) {
        this.messageQueue = new LinkedList<>();
        if (attributes != null) {
            this.attributes = attributes;
        } else {
            this.attributes = new AttributeCollection();
        }

        try {
            Object o = attributes.getAttribute(
                    TncCommonAttributeTypeEnum
                    .TNC_ATTRIBUTEID_MAX_ROUND_TRIPS);

            if (o instanceof Long) {
                this.maxRoundTrips = ((Long) o).longValue();
            }
        } catch (TncException | UnsupportedOperationException e) {
            this.maxRoundTrips = HSBConstants.TCG_IM_MAX_ROUND_TRIPS_UNKNOWN;
        }

        this.listener = listener;
        this.valid = true;
    }

    @Override
    public void addMessage(final TnccsMessage message) throws TncException {
        if (this.isValid()) {
            this.checkRoundTrips();
            this.messageQueue.add(message);
        } else {
            throw new TncException(
                    "Cannot add a message. "
                    + "Session and connection maybe closed.",
                    TncExceptionCodeEnum.TNC_RESULT_ILLEGAL_OPERATION);
        }
    }

    @Override
    public List<TnccsMessage> clearMessage() {
        List<TnccsMessage> messages = new LinkedList<>(this.messageQueue);
        this.messageQueue.clear();
        return messages;
    }

    @Override
    public void requestHandshakeRetry(final ImHandshakeRetryReasonEnum reason)
            throws TncException {
        this.checkRoundTrips();
        if (this.listener != null) {
            this.listener.retryHandshake(reason);
        }
    }

    @Override
    public Object getAttribute(final TncAttributeType type)
            throws TncException {
        return this.attributes.getAttribute(type);
    }

    @Override
    public void setAttribute(final TncAttributeType type,
            final Object value) throws TncException {
        this.attributes.setAttribute(type, value);
    }

    @Override
    public void validate() {
        if (!this.valid) {
            this.valid = true;
        }
    }

    @Override
    public void invalidate() {
        if (this.valid) {
            this.valid = false;
        }
    }

    @Override
    public boolean isValid() {
        return this.valid;
    }

    /**
     * Check if round trips are exceeded and no further messages
     * can be accepted on this connection.
     *
     * @throws TncException if round trips exceeded
     */
    protected void checkRoundTrips() throws TncException {
        if (HSBConstants.TCG_IM_MAX_ROUND_TRIPS_UNKNOWN < maxRoundTrips
                && maxRoundTrips < HSBConstants
                    .TCG_IM_MAX_ROUND_TRIPS_UNLIMITED) {

            try {
                Object o = attributes.getAttribute(
                        TncHsbAttributeTypeEnum
                        .HSB_ATTRIBUTEID_CURRENT_ROUND_TRIPS);

                if (o instanceof Long) {
                    long currentRoundTrips = ((Long) o).longValue();
                    if (currentRoundTrips >= this.maxRoundTrips) {
                        throw new TncException(
                                "Maximum round trips exceeded.",
                                TncExceptionCodeEnum
                                .TNC_RESULT_EXCEEDED_MAX_ROUND_TRIPS);
                    }
                }
            } catch (TncException | UnsupportedOperationException e) {
                LOGGER.debug(new StringBuilder()
                        .append("Custom attribute ")
                        .append(TncHsbAttributeTypeEnum
                        .HSB_ATTRIBUTEID_CURRENT_ROUND_TRIPS.toString())
                        .append(" not accessible. Round trip check cannot ")
                        .append("evaluate round trip count.").toString());
            }

        }
    }
}
