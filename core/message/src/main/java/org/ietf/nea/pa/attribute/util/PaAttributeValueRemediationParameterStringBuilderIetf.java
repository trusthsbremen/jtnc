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

import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;
import org.ietf.nea.pa.validate.rules.NoNullTerminatedString;
import org.ietf.nea.pa.validate.rules.NoZeroString;
import org.ietf.nea.pa.validate.rules.StringLengthLimit;

import de.hsbremen.tc.tnc.message.exception.RuleException;

/**
 * Builder to build an integrity measurement string remediation parameter value
 * compliant to RFC 5792. It evaluates the given values and can be used in a
 * fluent way.
 *
 *
 */
public class PaAttributeValueRemediationParameterStringBuilderIetf implements
        PaAttributeValueRemediationParameterStringBuilder {

    private long length;
    private String remediationString; // variable length, UTF-8 encoded, NUL
                                      // termination MUST NOT be included.
    private String langCode; // variable length, US-ASCII string composed of a
                             // well-formed RFC 4646 [3] language tag

    /**
     * Creates the builder using default values.
     * <ul>
     * <li>Length: Fixed value length only</li>
     * <li>String: ""</li>
     * <li>Language: ""</li>
     * </ul>
     */
    public PaAttributeValueRemediationParameterStringBuilderIetf() {
        this.length = PaAttributeTlvFixedLengthEnum.REM_PAR_STR.length();
        this.remediationString = "";
        this.langCode = "";
    }

    @Override
    public PaAttributeValueRemediationParameterStringBuilder
        setRemediationString(final String remediationString)
                throws RuleException {

        NoZeroString.check(remediationString);
        NoNullTerminatedString.check(remediationString);
        this.remediationString = remediationString;
        this.updateLength();

        return this;
    }

    @Override
    public PaAttributeValueRemediationParameterStringBuilder setLangCode(
            final String langCode) throws RuleException {

        // Zero length string for language code allowed.
        if (langCode != null) {
            NoNullTerminatedString.check(langCode);
            StringLengthLimit.check(langCode, 0xFF);
            this.langCode = langCode;
            this.updateLength();
        }

        return this;
    }

    @Override
    public PaAttributeValueRemediationParameterString toObject()
            throws RuleException {

        // check again because it has to be set properly
        NoZeroString.check(this.remediationString);

        return new PaAttributeValueRemediationParameterString(this.length,
                this.remediationString, this.langCode);
    }

    @Override
    public PaAttributeValueRemediationParameterStringBuilder newInstance() {

        return new PaAttributeValueRemediationParameterStringBuilderIetf();
    }

    /**
     * Updates the length according to the values set.
     */
    private void updateLength() {
        this.length = PaAttributeTlvFixedLengthEnum.REM_PAR_STR.length();
        if (remediationString.length() > 0) {
            this.length += remediationString.getBytes(
                    Charset.forName("UTF-8")).length;
        }
        if (langCode.length() > 0) {
            this.length += langCode.getBytes(
                    Charset.forName("US-ASCII")).length;
        }
    }
}
