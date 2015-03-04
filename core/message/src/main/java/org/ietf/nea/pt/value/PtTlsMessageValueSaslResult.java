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

import java.util.Arrays;

import org.ietf.nea.pt.value.enums.PtTlsSaslResultEnum;

/**
 * IETF RFC 6876 transport SASL result message value.
 *
 * @author Carl-Heinz Genzel
 *
 */
public class PtTlsMessageValueSaslResult extends AbstractPtTlsMessageValue {

    private final PtTlsSaslResultEnum result;
    private final byte[] resultData;

    /**
     * Creates the message value with the given values.
     *
     * @param length the value length
     * @param result the SASL authentication result
     */
    PtTlsMessageValueSaslResult(final long length,
            final PtTlsSaslResultEnum result) {
        this(length, result, new byte[0]);
    }

    /**
     * Creates the message value with the given values and additional data.
     *
     * @param length the value length
     * @param result the SASL authentication result
     * @param resultData the additional result data
     */
    PtTlsMessageValueSaslResult(final long length,
            final PtTlsSaslResultEnum result, final byte[] resultData) {
        super(length);
        this.result = result;
        this.resultData = (resultData != null) ? resultData : new byte[0];
    }

    /**
     * Returns the SASL authentication result.
     *
     * @return the authentication result
     */
    public PtTlsSaslResultEnum getResult() {
        return this.result;
    }

    /**
     * Returns the additional result data.
     *
     * @return the additional result data
     */
    public byte[] getResultData() {
        return Arrays.copyOf(this.resultData, this.resultData.length);
    }

}
