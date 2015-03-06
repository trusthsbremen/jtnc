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
 * Enumeration of known TNCCS assessment results.
 *
 *
 */
public enum PbMessageAssessmentResultEnum {

    /**
     * 0 - Posture Broker Server assessed the endpoint to be compliant with
     * policy.
     */
    COMPLIANT(0),
    /**
     * 1 - Posture Broker Server assessed the endpoint to be non- compliant with
     * policy but the difference from compliance was minor.
     */
    MINOR_DIFFERENCES(1),
    /**
     * 2 - Posture Broker Server assessed the endpoint to be non- compliant with
     * policy and the assessed difference from compliance was very significant.
     */
    SIGNIFICANT_DIFFERENCES(2),
    /**
     * 3 - Posture Broker Server was unable to determine policy compliance due
     * to an error.
     */
    ERROR(3),
    /**
     * 4 - Posture Broker Server was unable to determine whether the assessed
     * endpoint is compliant with policy based on the attributes provided by
     * endpoint.
     */
    INSUFFICIANT_ATTRIBUTES(4);

    private long id;

    /**
     * Creates the assessment result with the given result ID.
     * @param id the result ID
     */
    private PbMessageAssessmentResultEnum(final long id) {
        this.id = id;
    }

    /**
     * Returns the assessment result ID.
     *
     * @return the result ID
     */
    public long id() {
        return this.id;
    }

    /**
     * Returns the assessment result for the given result ID.
     *
     * @param id the result ID
     * @return a result or null
     */
    public static PbMessageAssessmentResultEnum fromId(final long id) {
        if (id == COMPLIANT.id) {
            return COMPLIANT;
        }
        if (id == MINOR_DIFFERENCES.id) {
            return MINOR_DIFFERENCES;
        }

        if (id == SIGNIFICANT_DIFFERENCES.id) {
            return SIGNIFICANT_DIFFERENCES;
        }

        if (id == ERROR.id) {
            return ERROR;
        }

        if (id == INSUFFICIANT_ATTRIBUTES.id) {
            return INSUFFICIANT_ATTRIBUTES;
        }

        return null;
    }
}
