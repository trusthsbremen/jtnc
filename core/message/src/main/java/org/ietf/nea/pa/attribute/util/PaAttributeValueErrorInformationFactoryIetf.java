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
package org.ietf.nea.pa.attribute.util;

import java.util.Arrays;

import org.ietf.nea.pa.attribute.PaAttributeHeader;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;

import de.hsbremen.tc.tnc.util.NotNull;

/**
 * Factory utility to create an IETF RFC 5792 compliant
 * error information.
 *
 *
 */
public abstract class PaAttributeValueErrorInformationFactoryIetf {

    /**
     * Creates an error information about an invalid
     * parameter.
     * @param messageHeader the byte copy of the message header of
     * the erroneous message
     * @param offset the offset from the message beginning to the
     * erroneous value
     * @return an IETF RFC 5792 compliant error information
     */
    public static PaAttributeValueErrorInformationInvalidParam
        createErrorInformationInvalidParameter(
            final byte[] messageHeader,
            final long offset) {

        NotNull.check("Message header cannot be null.", messageHeader);

        if (offset < 0) {
            throw new IllegalArgumentException("Offset cannot be negative.");
        }

        MessageHeaderDump header = parseHeader(messageHeader);

        long length = PaAttributeTlvFixedLengthEnum.MESSAGE.length() + 4;
        // 4 = offset length

        return new PaAttributeValueErrorInformationInvalidParam(length, header,
                offset);
    }

    /**
     * Creates an error information indicating an unsupported but mandatory
     * attribute.
     * @param messageHeader the byte copy of the message header of
     * the erroneous message
     * @param attributeHeader the copy of the header of the unsupported
     * attribute
     * @return an IETF RFC 5792 compliant error information
     */
    public static PaAttributeValueErrorInformationUnsupportedAttribute
        createErrorInformationUnsupportedAttribute(
            final byte[] messageHeader,
            final PaAttributeHeader attributeHeader) {

        NotNull.check("Message header cannot be null.", messageHeader);
        NotNull.check("Attribute header cannot be null.", attributeHeader);

        MessageHeaderDump header = parseHeader(messageHeader);

        long length = PaAttributeTlvFixedLengthEnum.MESSAGE.length()
                + PaAttributeTlvFixedLengthEnum.ATTRIBUTE.length() - 4;
        // - 4 = attribute length is ignored

        return new PaAttributeValueErrorInformationUnsupportedAttribute(length,
                header, attributeHeader);
    }

    /**
     * Creates an error information indicating supported versions.
     * @param messageHeader the byte copy of the message header of
     * the erroneous message
     * @param maxVersion the maximum version supported
     * @param minVersion the minimum version supported
     * @return an IETF RFC 5792 compliant error information
     */
    public static PaAttributeValueErrorInformationUnsupportedVersion
        createErrorInformationUnsupportedVersion(
            final byte[] messageHeader,
            final short maxVersion, final short minVersion) {

        NotNull.check("Message header cannot be null.", messageHeader);

        MessageHeaderDump header = parseHeader(messageHeader);

        long length = PaAttributeTlvFixedLengthEnum.MESSAGE.length() + 4;
        // 4 = min+max version length

        return new PaAttributeValueErrorInformationUnsupportedVersion(length,
                header, maxVersion, minVersion);
    }

    /**
     * Parses a message header to a message header object containing the
     * message header fields including any reserved fields.
     * @param messageHeader the byte data containing the message header
     * @return a message header object with all header fields
     */
    private static MessageHeaderDump parseHeader(final byte[] messageHeader) {

        byte[] sizedMessageHeader = Arrays.copyOf(messageHeader,
                PaAttributeTlvFixedLengthEnum.MESSAGE.length());

        short version = sizedMessageHeader[0];
        byte[] reserved = Arrays.copyOfRange(sizedMessageHeader, 1, 4);

        long value = 0L;
        byte[] b = Arrays.copyOfRange(sizedMessageHeader, 4, 8);
        for (int i = 0; i < b.length; i++) {
            value = (value << 8) + (b[i] & 0xFF);
        }
        long identifier = value;

        return new MessageHeaderDump(version, reserved, identifier);
    }
}
