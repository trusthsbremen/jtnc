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
package org.ietf.nea.pt.value;

import java.util.Arrays;

import org.ietf.nea.pt.value.enums.PtTlsSaslResultEnum;

/**
 * IETF RFC 6876 transport SASL result message value.
 *
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
