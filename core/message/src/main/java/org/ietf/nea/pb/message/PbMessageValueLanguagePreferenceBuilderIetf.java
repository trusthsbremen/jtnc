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
package org.ietf.nea.pb.message;

import java.nio.charset.Charset;

import org.ietf.nea.pb.validate.rules.NoNullTerminatedString;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessageValueBuilder;

/**
 * Builder to build a TNCCS language preference message value compliant to RFC
 * 5793. It evaluates the given values and can be used in a fluent way.
 *
 *
 */
public class PbMessageValueLanguagePreferenceBuilderIetf implements
        TnccsMessageValueBuilder, PbMessageValueLanguagePreferenceBuilder {

    private long length;
    private String languagePreference; // 32 bit(s), accept-Language header, as
                                       // described in RFC 3282 [4] as
                                       // Accept-Language included in that RFC,
                                       // US-ASCII only, no control characters
                                       // allowed, no comments, no NUL
                                       // termination)

    /**
     * Creates the builder using default values.
     * <ul>
     * <li>Length: 0</li>
     * <li>Preference: ""</li>
     * </ul>
     */
    public PbMessageValueLanguagePreferenceBuilderIetf() {
        this.length = 0;
        this.languagePreference = "";
    }

    @Override
    public PbMessageValueLanguagePreferenceBuilder setLanguagePreference(
            final String languagePreference) throws RuleException {

        // TODO regular expression test for language string (RFC 3282).

        NoNullTerminatedString.check(languagePreference);
        this.languagePreference = languagePreference;
        this.length = languagePreference.getBytes(
                Charset.forName("US-ASCII")).length;
        return this;
    }

    @Override
    public PbMessageValueLanguagePreference toObject() {

        return new PbMessageValueLanguagePreference(this.length,
                this.languagePreference);
    }

    @Override
    public PbMessageValueLanguagePreferenceBuilder newInstance() {
        return new PbMessageValueLanguagePreferenceBuilderIetf();
    }

}
