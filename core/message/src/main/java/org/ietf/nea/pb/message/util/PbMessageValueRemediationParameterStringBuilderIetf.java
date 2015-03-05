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
package org.ietf.nea.pb.message.util;

import java.nio.charset.Charset;

import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLengthEnum;
import org.ietf.nea.pb.validate.rules.LangCodeStringLimit;
import org.ietf.nea.pb.validate.rules.NoNullTerminatedString;
import org.ietf.nea.pb.validate.rules.NoZeroString;

import de.hsbremen.tc.tnc.message.exception.RuleException;

/**
 * Builder to build an TNCCS message string remediation parameter value
 * compliant to RFC 5793. It evaluates the given values and can be used in a
 * fluent way.
 *
 * @author Carl-Heinz Genzel
 *
 */
public class PbMessageValueRemediationParameterStringBuilderIetf implements
        PbMessageValueRemediationParameterStringBuilder {

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
    public PbMessageValueRemediationParameterStringBuilderIetf() {
        this.length = PbMessageTlvFixedLengthEnum.REM_STR_SUB_VALUE.length();
        this.remediationString = "";
        this.langCode = "";
    }

    @Override
    public PbMessageValueRemediationParameterStringBuilder setRemediationString(
            final String remediationString) throws RuleException {

        NoZeroString.check(remediationString);
        NoNullTerminatedString.check(remediationString);
        this.remediationString = remediationString;
        this.updateLength();

        return this;
    }

    @Override
    public PbMessageValueRemediationParameterStringBuilder setLangCode(
            final String langCode) throws RuleException {

        // Zero length string for language code allowed.
        if (langCode != null) {
            NoNullTerminatedString.check(langCode);
            LangCodeStringLimit.check(langCode);
            this.langCode = langCode;
            this.updateLength();
        }

        return this;
    }

    @Override
    public PbMessageValueRemediationParameterString toObject()
            throws RuleException {

        // check again because it has to set properly
        NoZeroString.check(this.remediationString);

        return new PbMessageValueRemediationParameterString(this.length,
                this.remediationString, this.langCode);
    }

    @Override
    public PbMessageValueRemediationParameterStringBuilder newInstance() {

        return new PbMessageValueRemediationParameterStringBuilderIetf();
    }

    /**
     * Updates the length according to the values set.
     */
    private void updateLength() {
        this.length = PbMessageTlvFixedLengthEnum.REM_STR_SUB_VALUE.length();
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
