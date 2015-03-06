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
package de.hsbremen.tc.tnc.report.enums;

/**
 * Enumeration of known action recommendations from an IMV.
 *
 *
 */
public enum ImvActionRecommendationEnum {

    /**
     * IMV recommends allowing access.
     */
    TNC_IMV_ACTION_RECOMMENDATION_ALLOW(0L),

    /**
     * IMV recommends no access.
     */
    TNC_IMV_ACTION_RECOMMENDATION_NO_ACCESS(1L),

    /**
     * IMV recommends limited access. This access may be expanded after
     * remediation.
     */
    TNC_IMV_ACTION_RECOMMENDATION_ISOLATE(2L),

    /**
     * IMV does not have a recommendation.
     */
    TNC_IMV_ACTION_RECOMMENDATION_NO_RECOMMENDATION(3L);

    private final long id;

    /**
     * Creates a action recommendation enumeration value with an ID.
     * @param id the recommendation ID
     */
    private ImvActionRecommendationEnum(final long id) {
        this.id = id;
    }

    /**
     * Returns the recommendation ID.
     * @return the recommendation ID
     */
    public long id() {
        return this.id;
    }

    /**
     * Returns a action recommendation for the given ID.
     *
     * @param id the recommendation ID
     * @return the action recommendation or null
     */
    public static ImvActionRecommendationEnum fromId(final long id) {

        if (id == TNC_IMV_ACTION_RECOMMENDATION_ALLOW.id) {
            return TNC_IMV_ACTION_RECOMMENDATION_ALLOW;
        }

        if (id == TNC_IMV_ACTION_RECOMMENDATION_NO_ACCESS.id) {
            return TNC_IMV_ACTION_RECOMMENDATION_NO_ACCESS;
        }

        if (id == TNC_IMV_ACTION_RECOMMENDATION_ISOLATE.id) {
            return TNC_IMV_ACTION_RECOMMENDATION_ISOLATE;
        }

        if (id == TNC_IMV_ACTION_RECOMMENDATION_NO_RECOMMENDATION.id) {
            return TNC_IMV_ACTION_RECOMMENDATION_NO_RECOMMENDATION;
        }

        return null;
    }

}
