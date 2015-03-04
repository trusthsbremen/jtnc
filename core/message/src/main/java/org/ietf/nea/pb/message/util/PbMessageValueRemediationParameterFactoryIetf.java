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

import java.net.URI;
import java.nio.charset.Charset;

import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLengthEnum;

import de.hsbremen.tc.tnc.util.NotNull;

/**
 * Factory utility to create a IETF RFC 5793 compliant
 * remediation parameter.
 *
 * @author Carl-Heinz Genzel
 *
 */
public abstract class PbMessageValueRemediationParameterFactoryIetf {

    /**
     * Private constructor should never be invoked.
     */
    private PbMessageValueRemediationParameterFactoryIetf() {
        throw new AssertionError();
    }

    /**
     * Creates a remediation parameter containing a human readable string
     * in the given language.
     * string.
     * @param remediationString the human readable string
     * @param langCode the RFC 4646 language code
     * @return an IETF RFC 5793 compliant remediation parameter
     */
    public static PbMessageValueRemediationParameterString
        createRemediationParameterString(
            final String remediationString, final String langCode) {

        String remediation = (remediationString != null)
                ? remediationString : "";
        String lang = (langCode != null) ? langCode : "";

        if (lang.length() > 0xFF) {
            throw new IllegalArgumentException("Language code length "
                    + lang.length() + "is to long.");
        }

        long length = PbMessageTlvFixedLengthEnum.REM_STR_SUB_VALUE.length();
        if (remediation.length() > 0) {
            length += remediation.getBytes(
                    Charset.forName("UTF-8")).length;
        }
        if (lang.length() > 0) {
            length += lang.getBytes(Charset.forName("US-ASCII")).length;
        }

        PbMessageValueRemediationParameterString parameter =
                new PbMessageValueRemediationParameterString(
                length, remediation, lang);

        return parameter;
    }

    /**
     * Creates a remediation  parameter with a remediation URI for further
     * information/actions.
     * @param uri the remediation URI
     * @return an IETF RFC 5793 compliant remediation parameter
     */
    public static PbMessageValueRemediationParameterUri
        createRemediationParameterUri(final String uri) {

        NotNull.check("URI cannot be null.", uri);

        URI temp = URI.create(uri);

        PbMessageValueRemediationParameterUri parameter =
                new PbMessageValueRemediationParameterUri(
                temp.toString().getBytes().length, temp);

        return parameter;
    }

}
