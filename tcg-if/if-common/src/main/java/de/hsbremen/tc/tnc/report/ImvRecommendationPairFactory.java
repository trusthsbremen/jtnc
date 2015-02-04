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

package de.hsbremen.tc.tnc.report;

import de.hsbremen.tc.tnc.report.enums.ImvActionRecommendationEnum;
import de.hsbremen.tc.tnc.report.enums.ImvEvaluationResultEnum;

/**
 * Factory utility to create an ImvRecommendationPair object.
 * @author Carl-Heinz Genzel
 *
 */
public abstract class ImvRecommendationPairFactory {

    private static final ImvRecommendationPair DEFAULT_PAIR =
            new ImvRecommendationPair();

    /**
     * Private constructor should never be invoked.
     */
    private ImvRecommendationPairFactory() {
        throw new AssertionError();
    }

    /**
     * Returns a static recommendation pair with default values.
     * <ul>
     * <li>Action recommendation = no recommendation</li>
     * <li>Evaluation result = dont't know</li>
     * </ul>
     *
     * @return the recommendation pair
     */
    public static ImvRecommendationPair getDefaultRecommendationPair() {
        return DEFAULT_PAIR;
    }

    /**
     * Creates a recommendation pair with the given
     * action recommendation and evaluation result.
     *
     * @param recommendation the action recommendation
     * @param result the evaluation result
     * @return the recommendation pair
     */
    public static ImvRecommendationPair createRecommendationPair(
            final ImvActionRecommendationEnum recommendation,
            final ImvEvaluationResultEnum result) {

        if (recommendation == null) {
            throw new NullPointerException(
                    "Action recommendation cannot be null.");
        }

        if (result == null) {
            throw new NullPointerException("Evaluation result cannot be null.");
        }

        return new ImvRecommendationPair(recommendation, result);
    }

    /**
     * Creates a recommendation pair with the given
     * action recommendation and evaluation result IDs.
     *
     * @param recommendation the action recommendation ID
     * @param result the evaluation result ID
     * @return the recommendation pair
     */
    public static ImvRecommendationPair createRecommendationPair(
            final long recommendation, final long result) {

        ImvActionRecommendationEnum rec = ImvActionRecommendationEnum
                .fromId(recommendation);
        if (rec == null) {
            throw new IllegalArgumentException("Action recommendation code "
                    + recommendation + " is unknown.");
        }

        ImvEvaluationResultEnum res = ImvEvaluationResultEnum
                .fromId(result);
        if (res == null) {
            throw new IllegalArgumentException("Evaluation result code "
                    + result + " is unknown.");
        }

        return new ImvRecommendationPair(rec, res);
    }

}
