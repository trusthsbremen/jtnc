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
 * Enumeration of known evaluation result values from an IMV.
 *
 *
 */
public enum ImvEvaluationResultEnum {

    /**
     * AR complies with policy.
     */
    TNC_IMV_EVALUATION_RESULT_COMPLIANT(0L),

    /**
     * AR is not compliant with policy. Non-compliance is minor.
     */
    TNC_IMV_EVALUATION_RESULT_NONCOMPLIANT_MINOR(1L),

    /**
     * AR is not compliant with policy. Non-compliance is major.
     */
    TNC_IMV_EVALUATION_RESULT_NONCOMPLIANT_MAJOR(2L),

    /**
     * IMV is unable to determine policy compliance due to error.
     */
    TNC_IMV_EVALUATION_RESULT_ERROR(3L),

    /**
     * IMV does not know whether AR complies with policy.
     */
    TNC_IMV_EVALUATION_RESULT_DONT_KNOW(4L);

    private final long id;

    /**
     * Creates a evaluation result enumeration value with an ID.
     * @param id the result ID
     */
    private ImvEvaluationResultEnum(final long id) {
        this.id = id;
    }

    /**
     * Returns the result ID.
     * @return the result ID
     */
    public long id() {
        return this.id;
    }

    /**
     * Returns an evaluation result for the given ID.
     *
     * @param id the result ID
     * @return the evaluation result or null
     */
    public static ImvEvaluationResultEnum fromId(final long id) {

        if (id == TNC_IMV_EVALUATION_RESULT_COMPLIANT.id) {
            return TNC_IMV_EVALUATION_RESULT_COMPLIANT;
        }

        if (id == TNC_IMV_EVALUATION_RESULT_NONCOMPLIANT_MINOR.id) {
            return TNC_IMV_EVALUATION_RESULT_NONCOMPLIANT_MINOR;
        }

        if (id == TNC_IMV_EVALUATION_RESULT_NONCOMPLIANT_MAJOR.id) {
            return TNC_IMV_EVALUATION_RESULT_NONCOMPLIANT_MAJOR;
        }

        if (id == TNC_IMV_EVALUATION_RESULT_ERROR.id) {
            return TNC_IMV_EVALUATION_RESULT_ERROR;
        }

        if (id == TNC_IMV_EVALUATION_RESULT_DONT_KNOW.id) {
            return TNC_IMV_EVALUATION_RESULT_DONT_KNOW;
        }

        return null;
    }
}
