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
package org.ietf.nea.pa.attribute.util;

import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;

/**
 * Builder to build an integrity measurement invalid parameter error
 * information value compliant to RFC 5792. It can be used in a fluent way.
 *
 *
 */
public class PaAttributeValueErrorInformationInvalidParamBuilderIetf implements
        PaAttributeValueErrorInformationInvalidParamBuilder {

    private long length;
    private MessageHeaderDump messageHeader;
    private long offset;

    /**
     * Creates the builder using default values.
     * <ul>
     * <li>Length: Fixed value length only</li>
     * <li>Message header: Dummy header</li>
     * <li>Offset: 0</li>
     * </ul>
     */
    public PaAttributeValueErrorInformationInvalidParamBuilderIetf() {
        final int fixedOffsetLength = 4;
        this.length = PaAttributeTlvFixedLengthEnum.ERR_INF.length()
                + PaAttributeTlvFixedLengthEnum.MESSAGE.length()
                + fixedOffsetLength;
        this.messageHeader = new MessageHeaderDump((short) 0, new byte[0], 0L);
        this.offset = 0;
    }

    @Override
    public PaAttributeValueErrorInformationInvalidParamBuilder setMessageHeader(
            final MessageHeaderDump messageHeader) {
        if (messageHeader != null) {
            this.messageHeader = messageHeader;
        }
        return this;
    }

    @Override
    public PaAttributeValueErrorInformationInvalidParamBuilder setOffset(
            final long offset) {
        this.offset = offset;
        return this;
    }

    @Override
    public PaAttributeValueErrorInformationInvalidParam toObject() {
        return new PaAttributeValueErrorInformationInvalidParam(length,
                messageHeader, offset);
    }

    @Override
    public PaAttributeValueErrorInformationInvalidParamBuilder newInstance() {
        return new PaAttributeValueErrorInformationInvalidParamBuilderIetf();
    }

}
