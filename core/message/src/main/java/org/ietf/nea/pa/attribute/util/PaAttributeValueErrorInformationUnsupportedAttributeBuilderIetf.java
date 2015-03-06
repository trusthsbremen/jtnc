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

import org.ietf.nea.pa.attribute.PaAttributeHeader;
import org.ietf.nea.pa.attribute.PaAttributeHeaderBuilderIetf;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;

/**
 * Builder to build an integrity measurement unsupported attribute error
 * information value compliant to RFC 5792. It can be used in a fluent way.
 *
 *
 */
public class PaAttributeValueErrorInformationUnsupportedAttributeBuilderIetf
        implements PaAttributeValueErrorInformationUnsupportedAttributeBuilder {

    private long length;
    private MessageHeaderDump messageHeader;
    private PaAttributeHeader attributeHeader;

    /**
     * Creates the builder using default values.
     * <ul>
     * <li>Length: Fixed value length only</li>
     * <li>Message header: Dummy header</li>
     * <li>Attribute header: Testing header</li>
     * </ul>
     */
    public PaAttributeValueErrorInformationUnsupportedAttributeBuilderIetf() {
        final int attributeLengthIgnoreOffset = 4;
        this.length = PaAttributeTlvFixedLengthEnum.ERR_INF.length()
                + PaAttributeTlvFixedLengthEnum.MESSAGE.length()
                + PaAttributeTlvFixedLengthEnum.ATTRIBUTE.length()
                - attributeLengthIgnoreOffset;

        this.messageHeader = new MessageHeaderDump((short) 0, new byte[0], 0L);
        this.attributeHeader = new PaAttributeHeaderBuilderIetf().toObject();
    }

    @Override
    public PaAttributeValueErrorInformationUnsupportedAttributeBuilder
        setMessageHeader(final MessageHeaderDump messageHeader) {
        if (messageHeader != null) {
            this.messageHeader = messageHeader;
        }
        return this;
    }

    @Override
    public PaAttributeValueErrorInformationUnsupportedAttributeBuilder
        setAttributeHeader(final PaAttributeHeader attributeHeader) {
        if (attributeHeader != null) {
            this.attributeHeader = attributeHeader;
        }
        return this;
    }

    @Override
    public PaAttributeValueErrorInformationUnsupportedAttribute toObject() {

        return new PaAttributeValueErrorInformationUnsupportedAttribute(
                this.length, this.messageHeader, this.attributeHeader);
    }

    @Override
    public PaAttributeValueErrorInformationUnsupportedAttributeBuilder
        newInstance() {
        return new PaAttributeValueErrorInformationUnsupportedAttributeBuilderIetf();
    }

}
