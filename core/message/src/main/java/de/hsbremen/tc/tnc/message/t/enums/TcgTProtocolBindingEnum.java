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
