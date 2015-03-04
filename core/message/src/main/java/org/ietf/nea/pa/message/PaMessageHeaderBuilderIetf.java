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
package org.ietf.nea.pa.message;

import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;
import org.ietf.nea.pa.validate.rules.CommonLengthLimits;
import org.ietf.nea.pa.validate.rules.MessageVersion;

import de.hsbremen.tc.tnc.message.exception.RuleException;

/**
 * Builder to build an integrity measurement component message header
 * compliant to RFC 5792. It can be used in a fluent way.
 *
 * @author Carl-Heinz Genzel
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
