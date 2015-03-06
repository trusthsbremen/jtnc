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
