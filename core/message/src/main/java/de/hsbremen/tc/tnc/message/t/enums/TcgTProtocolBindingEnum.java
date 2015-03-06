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
package de.hsbremen.tc.tnc.message.t.enums;

import de.hsbremen.tc.tnc.message.TcgProtocolBindingIdentifier;

/**
 * Enumeration containing the protocol names for IF-T protocol bindings.
 *
 *
 */
public enum TcgTProtocolBindingEnum implements TcgProtocolBindingIdentifier {

    /**
     * TCG IF-T binding for Tunneled EAP Methods version 1.0.
     */
    TEAP1("IF-T for Tunneled EAP", "1.0"),

    /**
     * TCG IF-T binding for Tunneled EAP Methods version 1.1.
     */
    TEAP1_1("IF-T for Tunneled EAP", "1.1"),

    /**
     * TCG binding to TLS version 2.0 aliased by IETF to 1.0.
     */
    TLS1("IF-T for TLS", "1.0"),

    /**
     * TCG IF-T binding for Tunneled EAP Methods using
     * Protected EAP version 1.0.
     */
    PEAP1("PEAP", "1.0"),

    /**
     * TCG IF-T binding for Tunneled EAP Methods using
     * Protected EAP version 1.1.
     */
    PEAP1_1("PEAP", "1.1"),

    /**
     * HSB IF-T binding for testing purpose using no encryption
     * only version is version 1.0.
     */
    PLAIN1("IF-T for Testing", "1.0");

    private final String label;
    private final String version;
    /**
     * Creates an enumeration with the given protocol
     * label and version.
     * @param label the protocol name
     * @param version the protocol version
     */
    private TcgTProtocolBindingEnum(final String label, final String version) {
        this.label = label;
        this.version = version;
    }

    @Override
    public String label() {
        return this.label;
    }

    @Override
    public String version() {
        return this.version;
    }

    @Override
    public String identifier() {
       return this.label + " " + this.version;
    }
}
