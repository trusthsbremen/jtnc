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
package org.ietf.nea.pt.value;

import org.ietf.nea.pt.message.enums.PtTlsMessageTlvFixedLengthEnum;
import org.ietf.nea.pt.validate.rules.VersionLimits;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.message.exception.RuleException;

/**
 * Builder to build a transport version request message value compliant to RFC
 * 6876. It evaluates the given values and can be used in a fluent way.
 *
 *
 */
public class PtTlsMessageValueVersionRequestBuilderIetf implements
        PtTlsMessageValueVersionRequestBuilder {

    private static final short PREFERRED_VERSION =
            IETFConstants.IETF_RFC6876_VERSION_NUMBER;

    private long length;
    private short preferredVersion;
    private short maxVersion;
    private short minVersion;

    /**
     * Creates the builder using default values.
     * <ul>
     * <li>Length: Fixed value length only</li>
     * <li>Preferred version: RFC 6876 version number</li>
     * <li>Maximum version: RFC 6876 version number</li>
     * <li>Minimum version: RFC 6876 version number</li>
     * </ul>
     */
    public PtTlsMessageValueVersionRequestBuilderIetf() {
        this.length = PtTlsMessageTlvFixedLengthEnum.VER_REQ.length();
        this.preferredVersion = PREFERRED_VERSION;
        this.maxVersion = PREFERRED_VERSION;
        this.minVersion = PREFERRED_VERSION;
    }

    @Override
    public PtTlsMessageValueVersionRequestBuilder setPreferredVersion(
            final short version) throws RuleException {

        VersionLimits.check(version);
        this.preferredVersion = version;
        return this;
    }

    @Override
    public PtTlsMessageValueVersionRequestBuilder setMaxVersion(
            final short version) throws RuleException {

        VersionLimits.check(version);
        this.maxVersion = version;
        return this;
    }

    @Override
    public PtTlsMessageValueVersionRequestBuilder setMinVersion(
            final short version) throws RuleException {

        VersionLimits.check(version);
        this.minVersion = version;
        return this;
    }

    @Override
    public PtTlsMessageValueVersionRequest toObject() {

        return new PtTlsMessageValueVersionRequest(length, preferredVersion,
                maxVersion, minVersion);
    }

    @Override
    public PtTlsMessageValueVersionRequestBuilder newInstance() {

        return new PtTlsMessageValueVersionRequestBuilderIetf();
    }

}
