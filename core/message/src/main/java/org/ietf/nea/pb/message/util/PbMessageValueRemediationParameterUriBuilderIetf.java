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
package org.ietf.nea.pb.message.util;

import java.net.URI;
import java.net.URISyntaxException;

import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.validate.enums.PbErrorCauseEnum;
import org.ietf.nea.pb.validate.rules.NoZeroString;

import de.hsbremen.tc.tnc.message.exception.RuleException;

/**
 * Builder to build an TNCCS message URI remediation parameter value
 * compliant to RFC 5793. It evaluates the given values and can be used in a
 * fluent way.
 *
 *
 */
public class PbMessageValueRemediationParameterUriBuilderIetf implements
        PbMessageValueRemediationParameterUriBuilder {

    private long length;
    private URI uri;

    /**
     * Creates the builder using default values.
     * <ul>
     * <li>Length: 0</li>
     * <li>URI: null</li>
     * </ul>
     */
    public PbMessageValueRemediationParameterUriBuilderIetf() {
        this.length = 0;
        this.uri = null;
    }

    @Override
    public PbMessageValueRemediationParameterUriBuilder setUri(final String uri)
            throws RuleException {

        NoZeroString.check(uri);
        try {
            this.uri = new URI(uri);
        } catch (URISyntaxException e) {
            throw new RuleException("URI syntax not valid.", e, true,
                    PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),
                    PbErrorCauseEnum.URI_SYNTAX_NOT_VALID.id(), uri);
        }
        this.length = this.uri.toString().getBytes().length;
        return this;
    }

    @Override
    public PbMessageValueRemediationParameterUri toObject()
            throws RuleException {

        if (uri == null) {
            throw new IllegalStateException("An uri value has to be set.");
        }

        return new PbMessageValueRemediationParameterUri(this.length, this.uri);
    }

    @Override
    public PbMessageValueRemediationParameterUriBuilder newInstance() {

        return new PbMessageValueRemediationParameterUriBuilderIetf();
    }

}
