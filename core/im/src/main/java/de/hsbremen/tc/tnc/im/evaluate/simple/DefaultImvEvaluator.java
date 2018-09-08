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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.im.evaluate.AbstractImEvaluatorIetf;
import de.hsbremen.tc.tnc.im.evaluate.ImValueExceptionHandler;
import de.hsbremen.tc.tnc.im.evaluate.ImvEvaluationUnit;
import de.hsbremen.tc.tnc.im.evaluate.ImvEvaluator;
import de.hsbremen.tc.tnc.im.session.ImSessionContext;
import de.hsbremen.tc.tnc.report.ImvRecommendationPair;
import de.hsbremen.tc.tnc.report.ImvRecommendationPairFactory;
import de.hsbremen.tc.tnc.report.LeastPrivilegeRecommendationComparator;

/**
 * Default IMV evaluator.
 *
 *
 */
public class DefaultImvEvaluator extends AbstractImEvaluatorIetf implements
        ImvEvaluator {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(DefaultImvEvaluator.class);

    /**
     * Creates the default evaluator using the given evaluation units and the
     * exception handler.
     *
     * @param id the evaluator ID identifying the component instance.
     * @param evaluationUnits the list of evaluations units
     * @param exceptionHandler the exception handler
     */
    public DefaultImvEvaluator(final long id,
            final List<ImvEvaluationUnit> evaluationUnits,
            final ImValueExceptionHandler exceptionHandler) {
        super(id, evaluationUnits, exceptionHandler);
    }

    @SuppressWarnings("unchecked")
    @Override
    public ImvRecommendationPair getRecommendation(
            final ImSessionContext context) {
        LOGGER.debug("getRecommendation() called, with connection state: "
                + context.getConnectionState().toString());

        List<ImvRecommendationPair> recommendations = new LinkedList<>();
        // cast save here because it must be initialized with ImvEvaluationUnits
        for (ImvEvaluationUnit unit : (List<ImvEvaluationUnit>) super
                .getEvaluationUnits()) {
            ImvRecommendationPair recO = ((ImvEvaluationUnit) unit)
                    .getRecommendation(context);
            if (recO != null) {
                recommendations.add(recO);
            }
        }

        if (!recommendations.isEmpty()) {

            Collections.sort(recommendations,
                    LeastPrivilegeRecommendationComparator.getInstance());
            // because of the sort get last from list which should be the most
            // severe
            return recommendations.get((recommendations.size() - 1));
        } else {
            // Defaults to don't know.
            return ImvRecommendationPairFactory.getDefaultRecommendationPair();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean hasRecommendation(ImSessionContext context) {

        // cast save here because it must be initialized with ImvEvaluationUnits
        for (ImvEvaluationUnit unit : (List<ImvEvaluationUnit>) super
                .getEvaluationUnits()) {
            if (!((ImvEvaluationUnit) unit).hasRecommendation(context)) {
                return false;
            }
        }

        return true;
    }
}
