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
package org.ietf.nea.pb.message.enums;

/**
 * Enumeration of known TNCCS access recommendations.
 *
 * @author Carl-Heinz Genzel
 *
 */
public enum PbMessageAccessRecommendationEnum {

    /**
     * 1 - Access Allowed - Full access is recommended.
     */
    ALLOWED(1),
    /**
     * 2 - Access Denied - No access is recommended.
     */
    DENIED(2),
    /**
     * 3 - Quarantined - Partial access is recommended to enable certain
     * activities such as updates.
     */
    QURANTINED(3);

    private int id;

    /**
     * Creates the access recommendation with the given result ID.
     * @param id the result ID
     */
    private PbMessageAccessRecommendationEnum(final int id) {
        this.id = id;
    }

    /**
     * Returns the access recommendation ID.
     *
     * @return the recommendation ID
     */
    public int id() {
        return this.id;
    }

    /**
     * Returns the access recommendation for the given
     * recommendation ID.
     *
     * @param id the recommendation ID
     * @return a recommendation or null
     */
    public static PbMessageAccessRecommendationEnum fromId(final int id) {

        if (id == ALLOWED.id) {
            return ALLOWED;
        }

        if (id == QURANTINED.id) {
            return QURANTINED;
        }

        if (id == DENIED.id) {
            return DENIED;
        }

        return null;
    }
}
