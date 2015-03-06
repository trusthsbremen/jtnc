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

import java.util.Comparator;

/**
 * Simple recommendation comparator comparing recommendations based on the least
 * privilege principle.
 *
 *
 */
public final class LeastPrivilegeRecommendationComparator implements
        Comparator<ImvRecommendationPair> {

    /**
     * Singleton to instantiate the factory only on first access.
     *
         *
     */
    private static class Singleton {
        private static final Comparator<ImvRecommendationPair> INSTANCE =
                new LeastPrivilegeRecommendationComparator();
    }

    /**
     * Returns the singleton instance of this factory.
     * @return the factory
     */
    public static Comparator<ImvRecommendationPair> getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * Creates the least privilege comparator.
     */
    private LeastPrivilegeRecommendationComparator() {
        // Singleton
    }

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

