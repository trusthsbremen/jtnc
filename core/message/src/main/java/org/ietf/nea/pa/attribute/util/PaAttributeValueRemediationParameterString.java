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
package org.ietf.nea.pa.attribute.util;

import java.nio.charset.Charset;

/**
 * IETF RFC 5792 integrity measurement string remediation parameter.
 *
 * @author Carl-Heinz Genzel
 *
 */
public class PaAttributeValueRemediationParameterString extends
        AbstractPaAttributeValueRemediationParameter {

    private final long stringLength; // 32 bit(s) length of the string in octets
    private final String remediationString; // variable length, UTF-8 encoded,
                                            // NUL termination MUST NOT be
                                            // included.
    private final short langCodeLength; // 8 bit(s) length of language code in
                                        // octets, 0 = language unknown
    private final String langCode; // variable length, US-ASCII string composed
                                   // of a well-formed RFC 4646 [3] language tag

    /**
     * Creates the parameter with the given values.
     *
     * @param length the parameter length
     * @param remediationString the remediation string
     * @param langCode the RFC4646 language identifier
     */
    PaAttributeValueRemediationParameterString(final long length,
            final String remediationString, final String langCode) {
        super(length);
        this.remediationString = remediationString;
        this.stringLength = remediationString.getBytes(
                        Charset.forName("UTF-8")).length;
        this.langCode = langCode;
        this.langCodeLength = (byte) langCode.getBytes(
                Charset.forName("US-ASCII")).length;
    }

    /**
     * Returns the remediation string length.
     *
     * @return the string length
     */
    public long getStringLength() {
        return this.stringLength;
    }

    /**
     * Returns the remediation string.
     *
     * @return the remediation string
     */
    public String getRemediationString() {
        return this.remediationString;
    }

    /**
     * Returns the language code length.
     *
     * @return the language code length
     */
    public short getLangCodeLength() {
        return this.langCodeLength;
    }

    /**
     * Returns the RFC4646 language identifier.
     *
     * @return the language code
     */
    public String getLangCode() {
        return this.langCode;
    }
}