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
