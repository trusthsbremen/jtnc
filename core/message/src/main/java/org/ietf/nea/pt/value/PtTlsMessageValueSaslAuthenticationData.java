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

/**
 * IETF RFC 6876 transport SASL authentication data message value.
 *
 *
 */
public class PtTlsMessageValueSaslAuthenticationData extends
        AbstractPtTlsMessageValue {

    private final byte[] authenticationData;

    /**
     * Creates the message value with the given values.
     *
     * @param length the value length
     * @param authenticationData the SASL authentication data
     */
    PtTlsMessageValueSaslAuthenticationData(final long length,
            final byte[] authenticationData) {
        super(length);
        this.authenticationData = (authenticationData != null)
                ? authenticationData : new byte[0];
    }

    /**
     * Returns the SASL authentication data.
     *
     * @return the authentication data
     */
    public byte[] getAuthenticationData() {
        return Arrays.copyOf(this.authenticationData,
                this.authenticationData.length);
    }

}
