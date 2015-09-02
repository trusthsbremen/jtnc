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
package org.ietf.nea.pa.message;

import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;
import org.ietf.nea.pa.validate.rules.CommonLengthLimits;
import org.ietf.nea.pa.validate.rules.MessageVersion;

import de.hsbremen.tc.tnc.message.exception.RuleException;

/**
 * Builder to build an integrity measurement component message header
 * compliant to RFC 5792. It evaluates the given values and can be
 * used in a fluent way.
 *
 *
 */
public class PaMessageHeaderBuilderIetf implements PaMessageHeaderBuilder {

    private static final byte SUPPORTED_VERSION = 1;

    private short version;
    private long identifier;
    private long length;

    /**
     * Creates the builder using default values.
     * <ul>
     * <li>Version: 1</li>
     * <li>Identifier: 0 </li>
     * <li>Length: Header length only</li>
     * </ul>
     */
    public PaMessageHeaderBuilderIetf() {
        this.version = SUPPORTED_VERSION;
        this.identifier = 0L;
        this.length = PaAttributeTlvFixedLengthEnum.MESSAGE.length();
    }

    @Override
    public PaMessageHeaderBuilder setVersion(final short version)
            throws RuleException {

        MessageVersion.check(version, SUPPORTED_VERSION);
        this.version = version;

        return this;
    }

    @Override
    public PaMessageHeaderBuilder setIdentifier(final long identifier)
            throws RuleException {

        // nothing to check here
        this.identifier = identifier;

        return this;
    }

    @Override
    public PaMessageHeaderBuilder setLength(final long length)
            throws RuleException {

        CommonLengthLimits.check(length);
        this.length = length;

        return this;
    }

    @Override
    public PaMessageHeader toObject() {

        return new PaMessageHeader(this.version, this.identifier, this.length);
    }

    @Override
    public PaMessageHeaderBuilder newInstance() {
        return new PaMessageHeaderBuilderIetf();
    }

}
