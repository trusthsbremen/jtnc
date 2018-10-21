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
import de.hsbremen.tc.tnc.report.enums.HandshakeRetryReasonEnum;
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
    public void requestHandshakeRetry(final HandshakeRetryReasonEnum reason)
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
        if (HSBConstants.TCG_IM_MAX_ROUND_TRIPS_UNKNOWN < this.maxRoundTrips
                && maxRoundTrips < HSBConstants
                    .TCG_IM_MAX_ROUND_TRIPS_UNLIMITED) {

            try {
                Object o = this.attributes.getAttribute(
                        TncHsbAttributeTypeEnum
                        .HSB_ATTRIBUTEID_CURRENT_ROUND_TRIPS);
                
                if (o instanceof Long) {
                    long currentRoundTrips = ((Long) o).longValue();
                    if (currentRoundTrips >= this.maxRoundTrips) {
                        throw new IllegalStateException("Maximum round trips exceeded.");
                    }
                }
                
            } catch (TncException | UnsupportedOperationException e) {
                LOGGER.debug(new StringBuilder()
                        .append("Custom attribute ")
                        .append(TncHsbAttributeTypeEnum
                        .HSB_ATTRIBUTEID_CURRENT_ROUND_TRIPS.toString())
                        .append(" not accessible. Round trip check cannot ")
                        .append("evaluate round trip count.").toString(), e);
            } catch (IllegalStateException e) {
                throw new TncException(e.getMessage(),
                        TncExceptionCodeEnum
                        .TNC_RESULT_EXCEEDED_MAX_ROUND_TRIPS);
            }
        }
    }
}
