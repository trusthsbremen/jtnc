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
