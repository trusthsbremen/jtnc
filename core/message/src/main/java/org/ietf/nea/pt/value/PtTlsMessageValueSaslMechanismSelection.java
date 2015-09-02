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
package org.ietf.nea.pt.value;

import java.util.Arrays;

import org.ietf.nea.pt.value.util.SaslMechanismEntry;

/**
 * IETF RFC 6876 transport SASL mechanism selection message value.
 *
 *
 */
public class PtTlsMessageValueSaslMechanismSelection extends
        AbstractPtTlsMessageValue {

    private final SaslMechanismEntry mechanism;
    private final byte[] initialSaslMsg;

    /**
     * Creates the message value with the given values.
     *
     * @param length the value length
     * @param mechanism the selected SASL mechanism
     */
    PtTlsMessageValueSaslMechanismSelection(final long length,
            final SaslMechanismEntry mechanism) {
        this(length, mechanism, new byte[0]);
    }

    /**
     * Creates the message value with the given values and initial SASL data.
     *
     * @param length the value length
     * @param mechanism the selected SASL mechanism
     * @param initialSaslMsg the initial SASL data
     */
    PtTlsMessageValueSaslMechanismSelection(final long length,
            final SaslMechanismEntry mechanism, final byte[] initialSaslMsg) {
        super(length);
        this.mechanism = mechanism;
        this.initialSaslMsg = (initialSaslMsg != null) ? initialSaslMsg
                : new byte[0];
    }

    /**
     * Returns the selected SASL mechanism.
     *
     * @return the SASL mechanism
     */
    public SaslMechanismEntry getMechanism() {
        return this.mechanism;
    }

    /**
     * Returns the initial SASL message.
     *
     * @return the initial SASL message
     */
    public byte[] getInitialSaslMsg() {
        return Arrays.copyOf(this.initialSaslMsg, this.initialSaslMsg.length);
    }

}
