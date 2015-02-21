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
package de.hsbremen.tc.tnc.im.evaluate;

import java.util.LinkedList;
import java.util.List;

import org.ietf.nea.pa.attribute.PaAttributeValueAssessmentResult;
import org.ietf.nea.pa.attribute.PaAttributeValueAttributeRequest;
import org.ietf.nea.pa.attribute.PaAttributeValueError;
import org.ietf.nea.pa.attribute.PaAttributeValueRemediationParameters;

import de.hsbremen.tc.tnc.im.adapter.GlobalHandshakeRetryListener;
import de.hsbremen.tc.tnc.im.session.ImSessionContext;
import de.hsbremen.tc.tnc.message.m.attribute.ImAttribute;
import de.hsbremen.tc.tnc.message.m.attribute.ImAttributeValue;

/**
 * Specialized base for an IMC evaluation unit. Especially important for
 * inheritance. It has predefined methods to handle mandatory message attributes
 * received from an IMV.
 *
 * @author Carl-Heinz Genzel
 *
 */
public abstract class AbstractImcEvaluationUnitIetf extends
        AbstractImEvaluationUnitIetf implements ImcEvaluationUnit {

    /**
     * Creates the specialized base for an IMC evaluation unit with the given
     * handshake retry listener.
     *
     * @param globalHandshakeRetryListener the global handshake retry listener.
     */
    protected AbstractImcEvaluationUnitIetf(
            final GlobalHandshakeRetryListener globalHandshakeRetryListener) {
        super(globalHandshakeRetryListener);
    }

    @Override
    public synchronized List<ImAttribute> handle(
            final List<? extends ImAttribute> attribute,
            final ImSessionContext context) {

        List<ImAttribute> attributes = new LinkedList<>();
        for (ImAttribute imAttribute : attribute) {
            ImAttributeValue value = imAttribute.getValue();
            if (value instanceof PaAttributeValueAssessmentResult) {
                attributes.addAll(this.handleResult(
                        (PaAttributeValueAssessmentResult) value, context));
            } else if (value instanceof PaAttributeValueRemediationParameters) {
                attributes
                        .addAll(this.handleRemediation(
                                (PaAttributeValueRemediationParameters) value,
                                context));
            } else if (value instanceof PaAttributeValueError) {
                attributes.addAll(this.handleError(
                        (PaAttributeValueError) value, context));
            } else if (value instanceof PaAttributeValueAttributeRequest) {
                attributes.addAll(this.handleAttributeRequest(
                        (PaAttributeValueAttributeRequest) value, context));
            }
        }
        return attributes;
    }

    /**
     * Handles an attribute request attribute and returns the requested
     * attributes if possible. Must be overwritten.
     *
     * @param value the attribute request
     * @param context the context, which holds connection specific values
     * @return a list with requested attributes
     */
    protected abstract List<? extends ImAttribute> handleAttributeRequest(
            PaAttributeValueAttributeRequest value, ImSessionContext context);

    /**
     * Handles an error attribute and returns other attributes in response
     * if needed. Must be overwritten.
     *
     * @param value the error
     * @param context the context, which holds connection specific values
     * @return a list with requested attributes
     */
    protected abstract List<? extends ImAttribute> handleError(
            PaAttributeValueError value, ImSessionContext context);

    /**
     * Handles a remediation instructions attribute and returns other
     * attributes in response if needed. Must be overwritten.
     *
     * @param value the remediation instructions
     * @param context the context, which holds connection specific values
     * @return a list with requested attributes
     */
    protected abstract List<? extends ImAttribute> handleRemediation(
            PaAttributeValueRemediationParameters value,
            ImSessionContext context);

    /**
     * Handles a result attribute and returns other attributes in response
     * if needed. Must be overwritten.
     *
     * @param value the result
     * @param context the context, which holds connection specific values
     * @return a list with requested attributes
     */
    protected abstract List<? extends ImAttribute> handleResult(
            PaAttributeValueAssessmentResult value, ImSessionContext context);

}
