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
