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
