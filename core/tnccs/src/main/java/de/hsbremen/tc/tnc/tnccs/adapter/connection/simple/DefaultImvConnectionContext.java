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
package de.hsbremen.tc.tnc.tnccs.adapter.connection.simple;

import java.util.HashMap;
import java.util.Map;

import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.exception.enums.TncExceptionCodeEnum;
import de.hsbremen.tc.tnc.report.ImvRecommendationPair;
import de.hsbremen.tc.tnc.tnccs.adapter.connection.AbstractImConnectionContext;
import de.hsbremen.tc.tnc.tnccs.adapter.connection
.ConnectionHandshakeRetryListener;
import de.hsbremen.tc.tnc.tnccs.adapter.connection.ImvConnectionContext;

/**
 * Default IMV connection context.
 *
 *
 */
public class DefaultImvConnectionContext extends AbstractImConnectionContext
        implements ImvConnectionContext {

    private final Map<Long, ImvRecommendationPair> recommendations;

    /**
     * Creates a default IMV connection context with the given
     * common session and/or connection attributes and the handshake
     * retry listener.
     *
     * @param attributes the common session/connection attributes
     * @param listener the handshake retry listener
     */
    public DefaultImvConnectionContext(final Attributed attributes,
            final ConnectionHandshakeRetryListener listener) {
        super(attributes, listener);
        this.recommendations = new HashMap<>();
    }

    @Override
    public void addRecommendation(final long id,
            final ImvRecommendationPair recommendationPair)
            throws TncException {

        if (super.isValid()) {
            this.recommendations.put(new Long(id), recommendationPair);
        } else {
            throw new TncException(
                    "Cannot add message. Session and connection may be closed.",
                    TncExceptionCodeEnum.TNC_RESULT_ILLEGAL_OPERATION);
        }

    }

    @Override
    public Map<Long, ImvRecommendationPair> clearRecommendations() {
        Map<Long, ImvRecommendationPair> r =
                new HashMap<>(this.recommendations);
        this.recommendations.clear();

        return r;
    }

}
