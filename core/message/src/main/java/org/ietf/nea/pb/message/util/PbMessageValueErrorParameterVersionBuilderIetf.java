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

import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLengthEnum;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.message.exception.RuleException;

/**
 * Builder to build a TNCCS message version error parameter value
 * compliant to RFC 5793. It can be used in a fluent way.
 *
 *
 */
public class PbMessageValueErrorParameterVersionBuilderIetf implements
        PbMessageValueErrorParameterVersionBuilder {

    private static final short PREFERRED_VERSION =
            IETFConstants.IETF_RFC5793_VERSION_NUMBER;

    private long length;
    private short badVersion;
    private short maxVersion;
    private short minVersion;

    /**
     * Creates the builder using default values.
     * <ul>
     * <li>Length: Fixed value length only</li>
     * <li>Bad version: 0</li>
     * <li>Maximum version: RFC 5793 version</li>
     * <li>Minimum version: RFC 5793 version</li>
     * </ul>
     */
    public PbMessageValueErrorParameterVersionBuilderIetf() {
        this.length = PbMessageTlvFixedLengthEnum.ERR_SUB_VALUE.length();
        this.badVersion = 0;
        this.maxVersion = PREFERRED_VERSION;
        this.minVersion = PREFERRED_VERSION;
    }

    @Override
    public PbMessageValueErrorParameterVersionBuilder setBadVersion(
            final short version) throws RuleException {
        this.badVersion = version;
        return this;
    }

    @Override
    public PbMessageValueErrorParameterVersionBuilder setMaxVersion(
            final short version) throws RuleException {
        this.maxVersion = version;
        return this;
    }

    @Override
    public PbMessageValueErrorParameterVersionBuilder setMinVersion(
            final short version) throws RuleException {
        this.minVersion = version;
        return this;
    }

    @Override
    public PbMessageValueErrorParameterVersion toObject() throws RuleException {

        return new PbMessageValueErrorParameterVersion(length, badVersion,
                maxVersion, minVersion);
    }

    @Override
    public PbMessageValueErrorParameterVersionBuilder newInstance() {

        return new PbMessageValueErrorParameterVersionBuilderIetf();
    }

}
