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
package org.ietf.nea.pa.attribute;

import org.ietf.nea.pa.attribute.enums.PaAttributeAssessmentResultEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;
import org.ietf.nea.pa.validate.rules.AssessmentResult;

import de.hsbremen.tc.tnc.message.exception.RuleException;

/**
 * Builder to build an integrity measurement assessment result attribute value
 * compliant to RFC 5792. It evaluates the given values and can be used in a
 * fluent way.
 *
 * @author Carl-Heinz Genzel
 *
 */
public class PaAttributeValueAssessmentResultBuilderIetf implements
        PaAttributeValueAssessmentResultBuilder {

    private long length;
    private PaAttributeAssessmentResultEnum result;

    /**
     * Creates the builder using default values.
     * <ul>
     * <li>Length: Fixed value length only</li>
     * <li>Result: Compliant</li>
     * </ul>
     */
    public PaAttributeValueAssessmentResultBuilderIetf() {
        this.length = PaAttributeTlvFixedLengthEnum.ASS_RES.length();
        this.result = PaAttributeAssessmentResultEnum.COMPLIANT;
    }

    @Override
    public PaAttributeValueAssessmentResultBuilder setResult(final long result)
            throws RuleException {

        AssessmentResult.check(result);
        this.result = PaAttributeAssessmentResultEnum.fromId(result);

        return this;
    }

    @Override
    public PaAttributeValueAssessmentResult toObject() {

        return new PaAttributeValueAssessmentResult(this.length, this.result);
    }

    @Override
    public PaAttributeValueAssessmentResultBuilder newInstance() {

        return new PaAttributeValueAssessmentResultBuilderIetf();
    }

}
