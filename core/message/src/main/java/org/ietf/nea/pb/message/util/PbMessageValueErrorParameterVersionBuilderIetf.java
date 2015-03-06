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

import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLengthEnum;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.message.exception.RuleException;

/**
 * Builder to build an TNCCS message version error parameter value
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
