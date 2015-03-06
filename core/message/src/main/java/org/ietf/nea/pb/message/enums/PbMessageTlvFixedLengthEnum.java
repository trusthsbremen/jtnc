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
package org.ietf.nea.pb.message.enums;

/**
 * Enumeration of fixed length values for different message parts. The values
 * are often minimal length values and my be larger in real. The length values
 * are not cumulated.
 *
 *
 */
public enum PbMessageTlvFixedLengthEnum {

    /**
     * Fixed batch header length.
     */
    BATCH((byte) 8),
    /**
     * Fixed message header length.
     */
    MESSAGE((byte) 12),
    /**
     * Fixed access recommendation value length.
     */
    ACC_REC_VALUE((byte) 4),
    /**
     * Fixed access result value length.
     */
    ASS_RES_VALUE((byte) 4),
    /**
     * Fixed error value length.
     */
    ERR_VALUE((byte) 8),
    /**
     * Fixed error parameter length.
     */
    ERR_SUB_VALUE((byte) 4),
    /**
     * Fixed integrity measurement component value length.
     */
    IM_VALUE((byte) 12),
    /**
     * Fixed reason string value length.
     */
    REA_STR_VALUE((byte) 5),
    /**
     * Fixed remediation value length.
     */
    REM_PAR_VALUE((byte) 8),
    /**
     * Fixed string remediation parameter length.
     */
    REM_STR_SUB_VALUE((byte) 5);

    private byte length;

    /**
     * Creates the fixed length value with the given length.
     *
     * @param length the fixed length
     */
    private PbMessageTlvFixedLengthEnum(final byte length) {
        this.length = length;
    }

    /**
     * Returns the fixed length.
     *
     * @return the fixed length
     */
    public byte length() {
        return this.length;
    }
}
