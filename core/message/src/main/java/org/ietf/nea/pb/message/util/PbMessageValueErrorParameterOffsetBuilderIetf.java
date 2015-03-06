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

import de.hsbremen.tc.tnc.message.exception.RuleException;

/**
 * Builder to build an TNCCS message error parameter value with an
 * error offset compliant to RFC 5793. It can be used in a fluent way.
 *
 *
 */
public class PbMessageValueErrorParameterOffsetBuilderIetf implements
        PbMessageValueErrorParameterOffsetBuilder {

    private long length;
    private long offset;

    /**
     * Creates the builder using default values.
     * <ul>
     * <li>Length: Fixed value length only</li>
     * <li>Offset: 0</li>
     * </ul>
     */
    public PbMessageValueErrorParameterOffsetBuilderIetf() {
        this.length = PbMessageTlvFixedLengthEnum.ERR_SUB_VALUE.length();
        this.offset = 0;
    }

    @Override
    public PbMessageValueErrorParameterOffsetBuilder setOffset(
            final long offset) throws RuleException {

        this.offset = offset;
        return this;
    }

    @Override
    public PbMessageValueErrorParameterOffset toObject() throws RuleException {

        return new PbMessageValueErrorParameterOffset(length, offset);
    }

    @Override
    public PbMessageValueErrorParameterOffsetBuilder newInstance() {

        return new PbMessageValueErrorParameterOffsetBuilderIetf();
    }

}
