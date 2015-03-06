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

import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;
import org.ietf.nea.pa.message.PaMessageHeader;
import org.ietf.nea.pa.message.PaMessageHeaderBuilder;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

/**
 * Reader to parse an integrity measurement component message header compliant
 * to RFC 5792 from a buffer of bytes to a Java object.
 *
 * @author Carl-Heinz Genzel
 *
 */
public class PaMessageHeaderReader implements ImReader<PaMessageHeader> {

    private PaMessageHeaderBuilder baseBuilder;

    /**
     * Creates the reader with a corresponding builder to validate the parsed
     * values and build the message header.
     *
     * @param builder the corresponding attribute value builder
     */
    PaMessageHeaderReader(final PaMessageHeaderBuilder builder) {
        this.baseBuilder = builder;
    }

    @Override
    public PaMessageHeader read(final ByteBuffer buffer, final long length)
            throws SerializationException, ValidationException {

        // ignore any given length and find out on your own.

        long errorOffset = 0;

        PaMessageHeader messageHeader = null;
        PaMessageHeaderBuilder builder =
                (PaMessageHeaderBuilder) this.baseBuilder.newInstance();

        try {
            try {

                /* version */
                errorOffset = buffer.bytesRead();
                byte version = buffer.readByte();
                builder.setVersion(version);

                /* ignore reserved */
                errorOffset = buffer.bytesRead();
                buffer.read(3);

                /* identifier */
                errorOffset = buffer.bytesRead();
                long identifier = buffer.readLong((byte) 4);
                builder.setIdentifier(identifier);

            } catch (BufferUnderflowException e) {
                throw new SerializationException("Data length "
                        + buffer.bytesWritten() + " in buffer to short.", e,
                        true, Long.toString(buffer.bytesWritten()));
            }

            messageHeader = (PaMessageHeader) builder.toObject();

        } catch (RuleException e) {
            throw new ValidationException(e.getMessage(), e, errorOffset);
        }

        return messageHeader;
    }

    @Override
    public byte getMinDataLength() {
        return PaAttributeTlvFixedLengthEnum.MESSAGE.length();
    }
}
