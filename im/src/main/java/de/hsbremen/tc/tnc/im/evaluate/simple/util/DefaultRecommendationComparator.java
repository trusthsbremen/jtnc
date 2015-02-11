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
package de.hsbremen.tc.tnc.im.evaluate.simple.util;

import java.util.Comparator;

import de.hsbremen.tc.tnc.report.ImvRecommendationPair;

/**
 * Simple recommendation comparator comparing recommendations based on the least
 * privilege principle.
 *
 * @author Carl-Heinz Genzel
 *
 */
public class DefaultRecommendationComparator implements
        Comparator<ImvRecommendationPair> {

    /**
     * Returns a weight for a action recommendation ID where the most negative
     * action has the highest weight.
     *
     * @param id the action recommendation ID
     * @return the weight value
     */
    private int weightImvAction(final long id) {

        if (id == 0) {
            return 1;
        }
        if (id == 1) {
            return 3;
        }
        if (id == 2) {
            return 2;
        }

        return 0;
    }

    /**
     * Returns a weight for an evaluation result ID where the most negative
     * result has the highest weight.
     *
     * @param id the result ID
     * @return the weight value
     */
    private int weightImvEvaluation(final long id) {

        if (id == 0) {
            return 2;
        }
        if (id == 1) {
            return 3;
        }
        if (id == 2) {
            return 4;
        }
        if (id == 3) {
            return 1;
        }

        return 0;
    }

    @Override
    public int compare(
            final ImvRecommendationPair o1,
            final ImvRecommendationPair o2) {

        long o1a = o1.getRecommendationId();
        long o2a = o2.getRecommendationId();

        int actionWeight = this.weightImvAction(o1a)
                - this.weightImvAction(o2a);
        if (actionWeight != 0) {
            return actionWeight;
        }

        long o1e = o1.getResultId();
        long o2e = o2.getResultId();
        return this.weightImvEvaluation(o1e) - this.weightImvEvaluation(o2e);

    }

}

