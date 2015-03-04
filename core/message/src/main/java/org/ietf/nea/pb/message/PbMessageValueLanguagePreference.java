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

/**
 * IETF RFC 5793 TNCCS language preference message value.
 *
 * @author Carl-Heinz Genzel
 *
 */
public class PbMessageValueLanguagePreference extends AbstractPbMessageValue {

    private final String preferedLanguage; // 32 bit(s), accept-Language header,
                                           // as described in RFC 3282 [4] as
                                           // Accept-Language included in that
                                           // RFC, US-ASCII only, no control
                                           // characters allowed, no comments,
                                           // no NUL termination)

    /**
     * Creates the message value with the given values.
     *
     * @param length the value length
     * @param preferedLanguage the RFC4646 language preference
     */
    PbMessageValueLanguagePreference(final long length,
            final String preferedLanguage) {
        super(length);
        this.preferedLanguage = preferedLanguage;
    }

    /**
     * Returns the RFC4646 language preference.
     * @return the RFC4646 language preference
     */
    public String getPreferedLanguage() {
        return this.preferedLanguage;
    }

    @Override
    public String toString() {
        return "PbMessageValueLanguagePreference [preferedLanguage="
                + this.preferedLanguage + "]";
    }

}
