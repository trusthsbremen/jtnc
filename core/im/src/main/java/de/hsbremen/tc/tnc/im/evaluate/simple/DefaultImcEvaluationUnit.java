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
package de.hsbremen.tc.tnc.im.evaluate.simple;

import java.util.ArrayList;
import java.util.List;

import org.ietf.nea.pa.attribute.PaAttributeValueAssessmentResult;
import org.ietf.nea.pa.attribute.PaAttributeValueAttributeRequest;
import org.ietf.nea.pa.attribute.PaAttributeValueError;
import org.ietf.nea.pa.attribute.PaAttributeValueRemediationParameters;
import org.ietf.nea.pa.attribute.util.AttributeReferenceEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trustedcomputinggroup.tnc.ifimc.TNCConstants;

import de.hsbremen.tc.tnc.im.adapter.GlobalHandshakeRetryListener;
import de.hsbremen.tc.tnc.im.evaluate.AbstractImcEvaluationUnitIetf;
import de.hsbremen.tc.tnc.im.evaluate.ImcEvaluationUnit;
import de.hsbremen.tc.tnc.im.session.ImSessionContext;
import de.hsbremen.tc.tnc.message.m.attribute.ImAttribute;

/**
 * Default IMC evaluation unit. Does only log.
 *
 *
 */
public class DefaultImcEvaluationUnit extends AbstractImcEvaluationUnitIetf
        implements ImcEvaluationUnit {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(DefaultImcEvaluationUnit.class);

    public static final long VENDOR_ID = TNCConstants.TNC_VENDORID_ANY;
    public static final long TYPE = TNCConstants.TNC_SUBTYPE_ANY;

    /**
     * Create a default evaluation unit with the given handshake retry listener.
     *
     * @param globalHandshakeRetryListener the global handshake retry listener.
     */
    public DefaultImcEvaluationUnit(
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
    protected List<? extends ImAttribute> handleAttributeRequest(
            final PaAttributeValueAttributeRequest value,
            final ImSessionContext context) {

        List<AttributeReferenceEntry> references = value.getReferences();
        StringBuilder b = new StringBuilder();
        if (references != null) {
            for (AttributeReferenceEntry ref : references) {
                b.append(ref.toString());
                b.append("\n");
            }
        }

        LOGGER.debug(new StringBuilder()
        .append("handleAttributeRequest() called, with connection state: ")
        .append(context.getConnectionState())
        .append("\n")
        .append("Requested attributes:\n")
        .append(b.toString()).toString());

        return new ArrayList<>(0);
    }

    @Override
    protected List<? extends ImAttribute> handleError(
            final PaAttributeValueError value,
            final ImSessionContext context) {
        LOGGER.debug(new StringBuilder()
        .append("handleError() called, with connection state: ")
        .append(context.getConnectionState())
        .append("\nError vendor ID (")
        .append(value.getErrorVendorId())
        .append("), error code (")
        .append(value.getErrorCode())
        .append("), and information type (")
        .append(value.getErrorInformation().getClass().getSimpleName())
        .append(").").toString());

        return new ArrayList<>(0);
    }

    @Override
    protected List<? extends ImAttribute> handleRemediation(
            final PaAttributeValueRemediationParameters value,
            final ImSessionContext context) {

        LOGGER.debug(new StringBuilder()
        .append("handleRemediation() called, with connection state: ")
        .append(context.getConnectionState())
        .append("\nRemediation vendor ID (")
        .append(value.getRpVendorId())
        .append("), remediation type (")
        .append(value.getRpType())
        .append("), and parameter type (")
        .append(value.getParameter().getClass().getSimpleName())
        .append(").").toString());

        return new ArrayList<>(0);
    }

    @Override
    protected List<? extends ImAttribute> handleResult(
            final PaAttributeValueAssessmentResult value,
            final ImSessionContext context) {

        LOGGER.debug(new StringBuilder()
        .append("handleResult() called, with connection state: ")
        .append(context.getConnectionState())
        .append("\nResult: ")
        .append(value.getResult().toString()).toString());

        return new ArrayList<>(0);
    }

    @Override
    public synchronized List<ImAttribute> lastCall(
            final ImSessionContext context) {

        LOGGER.debug(new StringBuilder()
        .append("lastCall() called, with connection state: ")
        .append(context.getConnectionState()).toString());

        return new ArrayList<>(0);
    }

}
