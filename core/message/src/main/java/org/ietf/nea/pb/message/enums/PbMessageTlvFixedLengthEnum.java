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
