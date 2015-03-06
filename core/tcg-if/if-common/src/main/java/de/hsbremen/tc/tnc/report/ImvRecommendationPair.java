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

/**
 * Entity which holds a integrity result pair from an IMV.
 *
 *
 */
public class ImvRecommendationPair {

    private final ImvActionRecommendationEnum recommendation;
    private final ImvEvaluationResultEnum result;

    /**
     * Creates a recommendation pair with default values.
     *
     * <ul>
     * <li>Action recommendation = no recommendation</li>
     * <li>Evalutaion result = dont't know</li>
     * </ul>
     */
    ImvRecommendationPair() {
        this.recommendation =
                ImvActionRecommendationEnum.
                TNC_IMV_ACTION_RECOMMENDATION_NO_RECOMMENDATION;
        this.result =
                ImvEvaluationResultEnum.TNC_IMV_EVALUATION_RESULT_DONT_KNOW;
    }

    /**
     * Creates a recommendation pair with the given
     * action recommendation and evaluation result.
     *
     * @param recommendation the action recommendation
     * @param result the evaluation result
     */
    ImvRecommendationPair(final ImvActionRecommendationEnum recommendation,
            final ImvEvaluationResultEnum result) {
        this.recommendation = recommendation;
        this.result = result;
    }

    /**
     * Returns the action recommendation.
     *
     * @return the recommendation
     */
    public ImvActionRecommendationEnum getRecommendation() {
        return this.recommendation;
    }

    /**
     * Returns the evaluation result.
     *
     * @return the result
     */
    public ImvEvaluationResultEnum getResult() {
        return this.result;
    }

    /**
     * Returns the ID of the contained action recommendation.
     *
     * @return the recommendation ID
     */
    public long getRecommendationId() {
        return this.recommendation.id();
    }

    /**
     * Returns the ID of the contained evaluation result.
     *
     * @return the result ID
     */
    public long getResultId() {
        return this.result.id();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        final int shiftFactor = 32;
        int result = 1;
        long recId = (this.recommendation == null) ? 0
                : this.recommendation.id();
        long evId = (this.result == null) ? 0 : this.result.id();
        result = prime
                * result
                + (int) (recId ^ (recId >>> shiftFactor));
//                + ((this.recommendation == null) ? 0 : this.recommendation
//                        .hashCode());

        result = prime * result
                + (int) (evId ^ (evId >>> shiftFactor));
//                + ((this.result == null) ? 0 : this.result.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ImvRecommendationPair other = (ImvRecommendationPair) obj;
        if (this.recommendation.id() != other.recommendation.id()) {
            return false;
        }
        if (this.result.id() != other.result.id()) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ImvRecommendationPair [recommendation="
                + this.recommendation.toString() + ", result="
                + this.result.toString() + "]";
    }

}
