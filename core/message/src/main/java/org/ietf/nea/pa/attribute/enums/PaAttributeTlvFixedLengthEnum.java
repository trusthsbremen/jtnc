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
package org.ietf.nea.pa.attribute.enums;

/**
 * Enumeration of fixed length values for different attributes parts. The values
 * are often minimal length values and my be larger in real. The length values
 * are not cumulated.
 *
 * @author Carl-Heinz Genzel
 *
 */
public enum PaAttributeTlvFixedLengthEnum {

    /**
     * Fixed message header length.
     */
    MESSAGE((byte) 8),
    /**
     * Fixed attribute header length.
     */
    ATTRIBUTE((byte) 12),
    /**
     * Fixed attribute request value length.
     */
    ATT_REQ((byte) 8),
    /**
     * Fixed product information value length.
     */
    PRO_INF((byte) 5),
    /**
     * Fixed numeric version value length.
     */
    NUM_VER((byte) 16),
    /**
     * Fixed string version value length.
     */
    STR_VER((byte) 3),
    /**
     * Fixed operational status value length.
     */
    OP_STAT((byte) 24),
    /**
     * Fixed port filter value length.
     */
    PORT_FT((byte) 4),
    /**
     * Fixed installed packages value length.
     */
    INS_PKG((byte) 4),
    /**
     * Fixed assessment result value length.
     */
    ASS_RES((byte) 4),
    /**
     * Fixed factory password status value length.
     */
    FAC_PW((byte) 4),
    /**
     * Fixed forwarding enabled status value length.
     */
    FWD_EN((byte) 4),
    /**
     * Fixed remediation value length.
     */
    REM_PAR((byte) 8),
    /**
     * Fixed string remediation parameter length.
     */
    REM_PAR_STR((byte) 5),
    /**
     * Fixed error information value length.
     */
    ERR_INF((byte) 8);

    private byte length;

    /**
     * Creates the fixed length value with the given length.
     *
     * @param length the fixed length
     */
    private PaAttributeTlvFixedLengthEnum(final byte length) {
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
