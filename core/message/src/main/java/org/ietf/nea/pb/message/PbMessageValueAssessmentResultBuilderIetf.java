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
package org.ietf.nea.pb.message;

import org.ietf.nea.pb.message.enums.PbMessageAssessmentResultEnum;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLengthEnum;
import org.ietf.nea.pb.validate.rules.AssessmentResult;

import de.hsbremen.tc.tnc.message.exception.RuleException;

/**
 * Builder to build a TNCCS assessment result message value compliant to RFC
 * 5793. It evaluates the given values and can be used in a fluent way.
 *
 * @author Carl-Heinz Genzel
 *
 */
public class PbMessageValueAssessmentResultBuilderIetf implements
        PbMessageValueAssessmentResultBuilder {

    private long length;
    private PbMessageAssessmentResultEnum result;

    /**
     * Creates the builder using default values.
     * <ul>
     * <li>Length: Fixed value length only</li>
     * <li>Result: Compliant</li>
     * </ul>
     */
    public PbMessageValueAssessmentResultBuilderIetf() {
        this.length = PbMessageTlvFixedLengthEnum.ASS_RES_VALUE.length();
        this.result = PbMessageAssessmentResultEnum.COMPLIANT;
    }

    @Override
    public PbMessageValueAssessmentResultBuilder setResult(final long result)
            throws RuleException {

        AssessmentResult.check(result);
        this.result = PbMessageAssessmentResultEnum.fromId(result);

        return this;
    }

    @Override
    public PbMessageValueAssessmentResult toObject() {

        return new PbMessageValueAssessmentResult(this.length, this.result);
    }

    @Override
    public PbMessageValueAssessmentResultBuilder newInstance() {
        return new PbMessageValueAssessmentResultBuilderIetf();
    }

}
