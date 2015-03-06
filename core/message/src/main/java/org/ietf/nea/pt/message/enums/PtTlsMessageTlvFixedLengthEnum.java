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
package org.ietf.nea.pt.message.enums;

/**
 * Enumeration of fixed length values for different message parts. The values
 * are often minimal length values and my be larger in real. The length
 * values are not cumulated.
 *
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
