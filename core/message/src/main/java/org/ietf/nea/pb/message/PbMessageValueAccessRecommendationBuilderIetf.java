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

import org.ietf.nea.pb.message.enums.PbMessageAccessRecommendationEnum;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLengthEnum;
import org.ietf.nea.pb.validate.rules.AccessRecommendation;

import de.hsbremen.tc.tnc.message.exception.RuleException;

/**
 * Builder to build a TNCCS access recommendation message value compliant to
 * RFC 5793. It evaluates the given values and can be used in a fluent way.
 *
 *
 */
public class PbMessageValueAccessRecommendationBuilderIetf implements
        PbMessageValueAccessRecommendationBuilder {

    private long length;
    private PbMessageAccessRecommendationEnum recommendation; // 16 bit(s)

    /**
     * Creates the builder using default values.
     * <ul>
     * <li>Length: Fixed value length only</li>
     * <li>Recommendation: Allowed</li>
     * </ul>
     */
    public PbMessageValueAccessRecommendationBuilderIetf() {
        this.length = PbMessageTlvFixedLengthEnum.ACC_REC_VALUE.length();
        this.recommendation = PbMessageAccessRecommendationEnum.ALLOWED;
    }

    @Override
    public PbMessageValueAccessRecommendationBuilder setRecommendation(
            final int recommendation) throws RuleException {

        AccessRecommendation.check(recommendation);
        this.recommendation = PbMessageAccessRecommendationEnum
                .fromId(recommendation);

        return this;
    }

    @Override
    public PbMessageValueAccessRecommendation toObject() {

        return new PbMessageValueAccessRecommendation(this.length,
                this.recommendation);
    }

    @Override
    public PbMessageValueAccessRecommendationBuilder newInstance() {

        return new PbMessageValueAccessRecommendationBuilderIetf();
    }

}
