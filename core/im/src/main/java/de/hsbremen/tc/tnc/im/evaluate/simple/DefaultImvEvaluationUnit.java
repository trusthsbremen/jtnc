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
package de.hsbremen.tc.tnc.im.evaluate.simple;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trustedcomputinggroup.tnc.ifimc.TNCConstants;

import de.hsbremen.tc.tnc.im.adapter.GlobalHandshakeRetryListener;
import de.hsbremen.tc.tnc.im.evaluate.AbstractImEvaluationUnitIetf;
import de.hsbremen.tc.tnc.im.evaluate.ImvEvaluationUnit;
import de.hsbremen.tc.tnc.im.session.ImSessionContext;
import de.hsbremen.tc.tnc.message.m.attribute.ImAttribute;
import de.hsbremen.tc.tnc.report.ImvRecommendationPair;
import de.hsbremen.tc.tnc.report.ImvRecommendationPairFactory;

/**
 * Default IMV evaluation unit. Does only log.
 *
 *
 */
public class DefaultImvEvaluationUnit extends AbstractImEvaluationUnitIetf
        implements ImvEvaluationUnit {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(DefaultImvEvaluationUnit.class);

    public static final long VENDOR_ID = TNCConstants.TNC_VENDORID_ANY;
    public static final long TYPE = TNCConstants.TNC_SUBTYPE_ANY;

    /**
     * Create a default evaluation unit with the given handshake retry listener.
     *
     * @param globalHandshakeRetryListener the global handshake retry listener.
     */
    public DefaultImvEvaluationUnit(
            final GlobalHandshakeRetryListener globalHandshakeRetryListener) {
        super(globalHandshakeRetryListener);
    }

    @Override
    public long getVendorId() {
        return VENDOR_ID;
    }

    @Override
    public long getType() {
        return TYPE;
    }

    @Override
    public synchronized List<ImAttribute> evaluate(
            final ImSessionContext context) {

        LOGGER.debug(new StringBuilder()
        .append("evaluate() called, with connection state: ")
        .append(context.getConnectionState()).toString());


        return new ArrayList<>(0);
    }

    @Override
    public void terminate() {
        LOGGER.debug("terminate() called.");

    }

    @Override
    public synchronized List<ImAttribute> lastCall(
            final ImSessionContext context) {

        LOGGER.debug(new StringBuilder()
        .append("lastCall() called, with connection state: ")
        .append(context.getConnectionState()).toString());

        return new ArrayList<>(0);
    }

    @Override
    public synchronized List<ImAttribute> handle(
            final List<? extends ImAttribute> components,
            final ImSessionContext context) {
        StringBuilder b = new StringBuilder();
        b.append("handleCall() called, with connection state: ")
                .append(context.getConnectionState()).append("\n");

        for (ImAttribute imAttribute : components) {
            b.append("Attribute received: [ID = ")

            .append(imAttribute.getHeader().getVendorId()).append(", Type = ")
                    .append(imAttribute.getHeader().getAttributeType())
                    .append(" ]\n");
        }

        LOGGER.debug(b.toString());

        return new ArrayList<>(0);
    }

    @Override
    public synchronized ImvRecommendationPair getRecommendation(
            final ImSessionContext context) {
        ImvRecommendationPair object = ImvRecommendationPairFactory
                .getDefaultRecommendationPair();
        LOGGER.debug(new StringBuilder()
        .append("getRecommendation() called, with connection state: ")
        .append(context.getConnectionState())
        .append("\n")
        .append(object.toString()).toString());

        return object;
    }

    @Override
    public boolean hasRecommendation(ImSessionContext context) {
        LOGGER.debug("hasRecommendation() called. - TRUE");
        return true;
    }

}
