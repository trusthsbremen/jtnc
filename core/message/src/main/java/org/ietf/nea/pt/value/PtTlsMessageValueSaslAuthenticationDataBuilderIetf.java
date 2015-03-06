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

import de.hsbremen.tc.tnc.message.exception.RuleException;

/**
 * Builder to build a transport SASL authentication data message value compliant
 * to RFC 6876. It evaluates the given values and can be used in a fluent way.
 *
 *
 */
public class PtTlsMessageValueSaslAuthenticationDataBuilderIetf implements
        PtTlsMessageValueSaslAuthenticationDataBuilder {

    private long length;
    private byte[] authenticationData; // variable string

    /**
     * Creates the builder using default values.
     * <ul>
     * <li>Length: 0</li>
     * <li>Data: null</li>
     * </ul>
     */
    public PtTlsMessageValueSaslAuthenticationDataBuilderIetf() {
        this.length = 0;
        this.authenticationData = null;
    }

    @Override
    public PtTlsMessageValueSaslAuthenticationDataBuilder setAuthenticationData(
            final byte[] data) throws RuleException {
        // no checks necessary because opaque
        if (data != null) {
            this.authenticationData = data;
            this.length = data.length;
        }

        return this;
    }

    @Override
    public PtTlsMessageValueSaslAuthenticationData toObject() {

        if (this.authenticationData == null) {
            throw new IllegalStateException(
                    "The SASL authentication data has to be set.");
        }

        return new PtTlsMessageValueSaslAuthenticationData(this.length,
                this.authenticationData);
    }

    @Override
    public PtTlsMessageValueSaslAuthenticationDataBuilder newInstance() {

        return new PtTlsMessageValueSaslAuthenticationDataBuilderIetf();
    }

}
