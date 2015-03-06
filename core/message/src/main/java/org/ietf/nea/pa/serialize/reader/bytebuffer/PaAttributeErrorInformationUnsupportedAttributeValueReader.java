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
package org.ietf.nea.pa.serialize.reader.bytebuffer;

import java.nio.BufferUnderflowException;

import org.ietf.nea.pa.attribute.PaAttributeHeader;
import org.ietf.nea.pa.attribute.PaAttributeHeaderBuilder;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;
import org.ietf.nea.pa.attribute.util
.PaAttributeValueErrorInformationUnsupportedAttribute;
import org.ietf.nea.pa.attribute.util
.PaAttributeValueErrorInformationUnsupportedAttributeBuilder;
import org.ietf.nea.pa.attribute.util.MessageHeaderDump;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

/**
 * Reader to parse an integrity measurement unsupported attribute error
 * information compliant to RFC 5792 from a buffer of bytes to a Java object.
 *
 * @author Carl-Heinz Genzel
 *
 */
class PaAttributeErrorInformationUnsupportedAttributeValueReader implements
        ImReader<PaAttributeValueErrorInformationUnsupportedAttribute> {

    private PaAttributeValueErrorInformationUnsupportedAttributeBuilder
        baseBuilder;
    private PaAttributeHeaderBuilder baseAttributeHeaderBuilder;

    /**
     * Creates the reader with a corresponding builder to validate the parsed
     * values and build the information. The second builder is used to
     * build the contained attribute header from the unsupported attribute.
     *
     * @param builder the corresponding information builder
     * @param attributeHeaderBuilder the attribute header builder
     */
    PaAttributeErrorInformationUnsupportedAttributeValueReader(
            final PaAttributeValueErrorInformationUnsupportedAttributeBuilder
                builder,
            final PaAttributeHeaderBuilder attributeHeaderBuilder) {
        this.baseBuilder = builder;
        this.baseAttributeHeaderBuilder = attributeHeaderBuilder;
    }

    @Override
    public PaAttributeValueErrorInformationUnsupportedAttribute read(
            final ByteBuffer buffer, final long messageLength)
            throws SerializationException, ValidationException {

        long errorOffset = 0;

        PaAttributeValueErrorInformationUnsupportedAttribute value = null;

        PaAttributeValueErrorInformationUnsupportedAttributeBuilder builder =
                (PaAttributeValueErrorInformationUnsupportedAttributeBuilder)
                this.baseBuilder.newInstance();
        PaAttributeHeaderBuilder attributeHeaderBuilder =
                (PaAttributeHeaderBuilder) this.baseAttributeHeaderBuilder
                .newInstance();

        try {

            try {

                errorOffset = buffer.bytesRead();
                /* message header */
                /* copy version */
                short version = buffer.readShort((byte) 1);

                /* copy reserved */
                byte[] reserved = buffer.read(3);

                /* copy identifier */
                long identifier = buffer.readLong((byte) 4);

                builder.setMessageHeader(new MessageHeaderDump(version,
                        reserved, identifier));

                /* max version */
                errorOffset = buffer.bytesRead();
                attributeHeaderBuilder.setFlags(buffer.readByte());

                /* attribute vendor ID */
                errorOffset = buffer.bytesRead();
                long vendorId = buffer.readLong((byte) 3);
                attributeHeaderBuilder.setVendorId(vendorId);

                /* attribute type */
                errorOffset = buffer.bytesRead();
                long type = buffer.readLong((byte) 4);
                attributeHeaderBuilder.setType(type);

                // set dummy length = 0
                attributeHeaderBuilder.setLength(0);

                PaAttributeHeader attributeHeader =
                        (PaAttributeHeader) attributeHeaderBuilder.toObject();
                builder.setAttributeHeader(attributeHeader);

            } catch (BufferUnderflowException e) {
                throw new SerializationException("Data length "
                        + buffer.bytesWritten() + " in buffer to short.", e,
                        true, Long.toString(buffer.bytesWritten()));
            }

            value = (PaAttributeValueErrorInformationUnsupportedAttribute)
                    builder.toObject();

        } catch (RuleException e) {
            throw new ValidationException(e.getMessage(), e, errorOffset);
        }

        return value;
    }

    @Override
    public byte getMinDataLength() {
        final int ignoredAttributeLength = 4;
        return (byte) (PaAttributeTlvFixedLengthEnum.ERR_INF.length()
                + PaAttributeTlvFixedLengthEnum.ATTRIBUTE.length()
                - ignoredAttributeLength);
    }
}
