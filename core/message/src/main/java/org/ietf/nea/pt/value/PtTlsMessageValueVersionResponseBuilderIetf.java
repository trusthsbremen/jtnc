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
package org.ietf.nea.pt.value;

import org.ietf.nea.pa.validate.rules.MessageVersion;
import org.ietf.nea.pt.message.enums.PtTlsMessageTlvFixedLengthEnum;
import org.ietf.nea.pt.validate.rules.VersionLimits;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.message.exception.RuleException;

/**
 * Builder to build a transport version response message value compliant to RFC
 * 6876. It evaluates the given values and can be used in a fluent way.
 *
 * @author Carl-Heinz Genzel
 *
 */
public class PtTlsMessageValueVersionResponseBuilderIetf implements
        PtTlsMessageValueVersionResponseBuilder {

    private static final short PREFERRED_VERSION =
            IETFConstants.IETF_RFC6876_VERSION_NUMBER;

    private long length;
    private short version;

    /**
     * Creates the builder using default values.
     * <ul>
     * <li>Length: Fixed value length only</li>
     * <li>Version: RFC 6876 version number</li>
     * </ul>
     */
    public PtTlsMessageValueVersionResponseBuilderIetf() {
        this.length = PtTlsMessageTlvFixedLengthEnum.VER_RES.length();
        this.version = PREFERRED_VERSION;
    }

    @Override
    public PtTlsMessageValueVersionResponseBuilder setVersion(
            final short version) throws RuleException {

        VersionLimits.check(version);
        MessageVersion.check(version, PREFERRED_VERSION);

        this.version = version;
        return this;
    }

    @Override
    public PtTlsMessageValueVersionResponse toObject() {

        return new PtTlsMessageValueVersionResponse(length, version);
    }

    @Override
    public PtTlsMessageValueVersionResponseBuilder newInstance() {

        return new PtTlsMessageValueVersionResponseBuilderIetf();
    }

}
