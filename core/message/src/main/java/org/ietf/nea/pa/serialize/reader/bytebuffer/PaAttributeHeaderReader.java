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

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

/**
 * Reader to parse an integrity measurement attribute header compliant to
 * RFC 5792 from a buffer of bytes to a Java object.
 *
 * @author Carl-Heinz Genzel
 *
 */
class PaAttributeHeaderReader implements ImReader<PaAttributeHeader> {

    private PaAttributeHeaderBuilder baseBuilder;

    /**
     * Creates the reader with a corresponding builder to validate the parsed
     * values and build the attribute header.
     *
     * @param builder the corresponding attribute header builder
     */
    PaAttributeHeaderReader(final PaAttributeHeaderBuilder builder) {
        this.baseBuilder = builder;
    }

    @Override
    public PaAttributeHeader read(final ByteBuffer buffer,
            final long messageLength) throws SerializationException,
            ValidationException {

        // ignore any given length and find out on your own.

        long errorOffset = 0;

        PaAttributeHeader messageHeader = null;
        PaAttributeHeaderBuilder builder =
                (PaAttributeHeaderBuilder) this.baseBuilder.newInstance();

        try {
            try {

                /* flags */
                errorOffset = buffer.bytesRead();
                builder.setFlags(buffer.readByte());

                /* vendor ID */
                errorOffset = buffer.bytesRead();
                long vendorId = buffer.readLong((byte) 3);
                builder.setVendorId(vendorId);

                /* attribute type */
                errorOffset = buffer.bytesRead();
                long messageType = buffer.readLong((byte) 4);
                builder.setType(messageType);

                /* attribute length */
                errorOffset = buffer.bytesRead();
                long length = buffer.readLong((byte) 4);
                builder.setLength(length);

            } catch (BufferUnderflowException e) {
                throw new SerializationException("Data length "
                        + buffer.bytesWritten() + " in buffer to short.", e,
                        true, Long.toString(buffer.bytesWritten()));
            }

            messageHeader = (PaAttributeHeader) builder.toObject();

        } catch (RuleException e) {
            throw new ValidationException(e.getMessage(), e, errorOffset);
        }

        return messageHeader;
    }

    @Override
    public byte getMinDataLength() {
        return PaAttributeTlvFixedLengthEnum.ATTRIBUTE.length();
    }

}
