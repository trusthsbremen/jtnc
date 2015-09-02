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
