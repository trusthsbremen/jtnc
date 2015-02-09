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
package de.hsbremen.tc.tnc.im.evaluate.example.simple;

import java.util.ArrayList;
import java.util.List;

import org.ietf.nea.pa.attribute.PaAttributeValueAssessmentResult;
import org.ietf.nea.pa.attribute.PaAttributeValueAttributeRequest;
import org.ietf.nea.pa.attribute.PaAttributeValueError;
import org.ietf.nea.pa.attribute.PaAttributeValueRemediationParameters;
import org.ietf.nea.pa.attribute.util.AttributeReference;
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
 * @author Carl-Heinz Genzel
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

        List<AttributeReference> references = value.getReferences();
        StringBuilder b = new StringBuilder();
        if (references != null) {
            for (AttributeReference ref : references) {
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
