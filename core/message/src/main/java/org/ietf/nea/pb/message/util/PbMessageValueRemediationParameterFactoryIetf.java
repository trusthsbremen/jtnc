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
package org.ietf.nea.pb.message.util;

import java.net.URI;
import java.nio.charset.Charset;

import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLengthEnum;

import de.hsbremen.tc.tnc.util.NotNull;

/**
 * Factory utility to create a IETF RFC 5793 compliant
 * remediation parameter.
 *
 *
 */
public final class PbMessageValueRemediationParameterFactoryIetf {

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
