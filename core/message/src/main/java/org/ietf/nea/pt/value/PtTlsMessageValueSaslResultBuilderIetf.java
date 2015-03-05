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
package org.ietf.nea.pt.value;

import org.ietf.nea.pt.message.enums.PtTlsMessageTlvFixedLengthEnum;
import org.ietf.nea.pt.validate.rules.SaslResult;
import org.ietf.nea.pt.value.enums.PtTlsSaslResultEnum;

import de.hsbremen.tc.tnc.message.exception.RuleException;

/**
 * Builder to build a transport SASL authentication result message value
 * compliant to RFC 6876. It evaluates the given values and can be used in a
 * fluent way.
 *
 * @author Carl-Heinz Genzel
 *
 */
public class PtTlsMessageValueSaslResultBuilderIetf implements
        PtTlsMessageValueSaslResultBuilder {

    private long length;
    private PtTlsSaslResultEnum result;
    private byte[] resultData;

    /**
     * Creates the builder using default values.
     * <ul>
     * <li>Length: Fixed value length only</li>
     * <li>Result: Abort</li>
     * <li>Data: null</li>
     * </ul>
     */
    public PtTlsMessageValueSaslResultBuilderIetf() {
        this.length = PtTlsMessageTlvFixedLengthEnum.SASL_RLT.length();
        this.result = PtTlsSaslResultEnum.ABORT;
        this.resultData = null;
    }

    @Override
    public PtTlsMessageValueSaslResultBuilder setResult(final int result)
            throws RuleException {

        SaslResult.check(result);
        this.result = PtTlsSaslResultEnum.fromId(result);

        return this;
    }

    @Override
    public PtTlsMessageValueSaslResultBuilder setOptionalResultData(
            final byte[] resultData) throws RuleException {

        if (resultData != null) {
            this.resultData = resultData;
        }

        return this;
    }

    @Override
    public PtTlsMessageValueSaslResult toObject() {

        if (this.resultData != null) {
            this.length += this.resultData.length;
            return new PtTlsMessageValueSaslResult(this.length, this.result,
                    this.resultData);
        } else {
            return new PtTlsMessageValueSaslResult(this.length, this.result);
        }
    }

    @Override
    public PtTlsMessageValueSaslResultBuilder newInstance() {
        return new PtTlsMessageValueSaslResultBuilderIetf();
    }

}
