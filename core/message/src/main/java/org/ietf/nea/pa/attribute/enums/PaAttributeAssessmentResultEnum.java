/**
 * The BSD 3-Clause License ("BSD New" or "BSD Simplified")
 *
 * Copyright © 2015 Trust HS Bremen and its Contributors. All rights   
 * reserved.
 *
 * See the CONTRIBUTORS file distributed with this work for additional
 * information regarding copyright ownership.
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
package org.ietf.nea.pa.attribute.enums;

/**
 * Enumeration of known integrity measurement assessment
 * results.
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
