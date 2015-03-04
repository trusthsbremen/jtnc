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
package org.ietf.nea.pt.message.enums;

/**
 * Enumeration of fixed length values for different message parts. The values
 * are often minimal length values and my be larger in real. The length
 * values are not cumulated.
 *
 * @author Carl-Heinz Genzel
 *
 */
public enum PtTlsMessageTlvFixedLengthEnum {

    /**
     * Fixed message header length.
     */
    MESSAGE((byte) 16),
    /**
     * Fixed version request value length.
     */
    VER_REQ((byte) 4),
    /**
     * Fixed version response value length.
     */
    VER_RES((byte) 4),
    /**
     * Fixed SASL selection value length.
     */
    SASL_SEL((byte) 1),
    /**
     * Fixed SASL result length.
     */
    SASL_RLT((byte) 2),
    /**
     * Fixed error value length.
     */
    ERR_VALUE((byte) 8);

    private byte length;

    /**
     * Creates the fixed length value with the given length.
     *
     * @param length the fixed length
     */
    private PtTlsMessageTlvFixedLengthEnum(final byte length) {
        this.length = length;
    }

    /**
     * Returns the fixed length.
     * @return the fixed length
     */
    public byte length() {
        return this.length;
    }
}
