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
 * Enumeration of known evaluation result values from an IMV.
 *
 * @author Carl-Heinz Genzel
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
