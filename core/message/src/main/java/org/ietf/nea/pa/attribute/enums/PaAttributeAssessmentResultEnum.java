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
package org.ietf.nea.pa.attribute.enums;

/**
 * Enumeration of known integrity measurement assessment
 * results.
 * @author Carl-Heinz Genzel
 *
 */
public enum PaAttributeAssessmentResultEnum {

    /**
     * 0 - Posture Validator assessed the endpoint component to be compliant
     * with policy.
     */
    COMPLIANT(0),
    /**
     * 1 - Posture Validator assessed the endpoint component to be non-compliant
     * with policy but the difference from compliant was minor.
     */
    MINOR_DIFFERENCES(1),
    /**
     * 2 - Posture Validator assessed the endpoint component to be non-compliant
     * with policy and the assessed difference was very significant.
     */
    SIGNIFICANT_DIFFERENCES(2),
    /**
     * 3 - Posture Validator was unable to determine policy compliance of an
     * endpoint component due to an error.
     */
    ERROR(3),
    /**
     * 4 - Posture Validator was unable to determine whether the assessed
     * endpoint component was compliant with policy based on the attributes
     * provided by the Posture Collector.
     */
    INSUFFICIANT_ATTRIBUTES(4);

    private long id;

    /**
     * Creates the assessment result with the given result ID.
     * @param id the result ID
     */
    private PaAttributeAssessmentResultEnum(final long id) {
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
    public static PaAttributeAssessmentResultEnum fromId(final long id) {
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
