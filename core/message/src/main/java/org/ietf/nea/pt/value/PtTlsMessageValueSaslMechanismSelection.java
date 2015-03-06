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
