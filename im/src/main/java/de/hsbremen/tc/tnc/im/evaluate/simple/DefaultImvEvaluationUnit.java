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
 * @author Carl-Heinz Genzel
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
    public boolean hasRecommendation() {
        LOGGER.debug("hasRecommendation() called. - TRUE");
        return true;
    }

}
