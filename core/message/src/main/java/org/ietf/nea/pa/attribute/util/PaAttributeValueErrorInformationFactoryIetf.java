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
