/**
 * The BSD 3-Clause License ("BSD New" or "BSD Simplified")
 *
 * Copyright © 2015 Trust HS Bremen and its Contributors. All rights   
 * reserved.
 *
 * See the CONTRIBUTORS file distributed with this work for additional
 * information regarding copyright ownership.
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
package org.ietf.nea.pa.attribute.enums;

/**
 * Enumeration of fixed length values for different attributes parts. The values
 * are often minimal length values and my be larger in real. The length values
 * are not cumulated.
 *
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
