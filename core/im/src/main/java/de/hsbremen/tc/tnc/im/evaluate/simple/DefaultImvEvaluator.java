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
 * @author Carl-Heinz Genzel
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
    public boolean hasRecommendation() {

        // cast save here because it must be initialized with ImvEvaluationUnits
        for (ImvEvaluationUnit unit : (List<ImvEvaluationUnit>) super
                .getEvaluationUnits()) {
            if (!((ImvEvaluationUnit) unit).hasRecommendation()) {
                return false;
            }
        }

        return true;
    }
}
