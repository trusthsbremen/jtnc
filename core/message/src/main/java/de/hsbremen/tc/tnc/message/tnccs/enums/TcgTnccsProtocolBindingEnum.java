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
package de.hsbremen.tc.tnc.message.tnccs.enums;

import de.hsbremen.tc.tnc.message.TcgProtocolBindingIdentifier;

/**
 * Enumeration containing the protocol names for IF-TNCCS protocol bindings.
 *
 *
 */
public enum TcgTnccsProtocolBindingEnum implements
    TcgProtocolBindingIdentifier {

    /**
     * TCG IF-TNCCS binding version 1.0.
     */
    TNCCS1("IF-TNCCS", "1.0"),
    /**
     * TCG IF-TNCCS binding version 1.1.
     */
    TNCCS1_1("IF-TNCCS", "2.0"),
    /**
     * TCG IF-TNCCS binding version 2.0.
     */
    TNCCS2("IF-TNCCS", "2.0"),

    /**
     * TCG IF-TNCCS binding for SoH version 1.0.
     */
    TNCCS_SOH1("IF-TNCCS-SOH", "1.0");

    private final String label;
    private final String version;
    /**
     * Creates an enumeration with the given protocol
     * label and version.
     * @param label the protocol name
     * @param version the protocol version
     */
    private TcgTnccsProtocolBindingEnum(final String label,
            final String version) {
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
