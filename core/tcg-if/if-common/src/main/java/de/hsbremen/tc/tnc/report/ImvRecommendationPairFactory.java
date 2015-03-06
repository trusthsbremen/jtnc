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

package de.hsbremen.tc.tnc.report;

import de.hsbremen.tc.tnc.report.enums.ImvActionRecommendationEnum;
import de.hsbremen.tc.tnc.report.enums.ImvEvaluationResultEnum;
import de.hsbremen.tc.tnc.util.NotNull;

/**
 * Factory utility to create an ImvRecommendationPair object.
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

        NotNull.check("Action recommendation cannot be null.", recommendation);

        NotNull.check("Evaluation result cannot be null.", result);

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
