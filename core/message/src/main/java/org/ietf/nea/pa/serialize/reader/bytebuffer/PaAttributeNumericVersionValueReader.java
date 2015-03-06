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

import org.ietf.nea.pa.attribute.PaAttributeValueNumericVersion;
import org.ietf.nea.pa.attribute.PaAttributeValueNumericVersionBuilder;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

/**
 * Reader to parse an integrity measurement numeric version attribute value
 * compliant to RFC 5792 from a buffer of bytes to a Java object.
 *
 *
 */
class PaAttributeNumericVersionValueReader implements
        ImReader<PaAttributeValueNumericVersion> {

    private PaAttributeValueNumericVersionBuilder baseBuilder;

    /**
     * Creates the reader with a corresponding builder to validate the parsed
     * values and build the attribute value.
     *
     * @param builder the corresponding attribute value builder
     */
    PaAttributeNumericVersionValueReader(
            final PaAttributeValueNumericVersionBuilder builder) {
        this.baseBuilder = builder;
    }

    @Override
    public PaAttributeValueNumericVersion read(final ByteBuffer buffer,
            final long messageLength) throws SerializationException,
            ValidationException {

        // ignore any given length and find out on your own.

        long errorOffset = 0;

        PaAttributeValueNumericVersion value = null;
        PaAttributeValueNumericVersionBuilder builder =
                (PaAttributeValueNumericVersionBuilder) baseBuilder
                .newInstance();

        try {
            try {

                /* major version */
                errorOffset = buffer.bytesRead();
                long majorVersion = buffer.readLong((byte) 4);
                builder.setMajorVersion(majorVersion);

                /* minor version */
                errorOffset = buffer.bytesRead();
                long minorVersion = buffer.readLong((byte) 4);
                builder.setMinorVersion(minorVersion);

                /* build version */
                errorOffset = buffer.bytesRead();
                long buildVersion = buffer.readLong((byte) 4);
                builder.setBuildVersion(buildVersion);

                /* service pack major */
                errorOffset = buffer.bytesRead();
                int servicePackMajor = buffer.readInt((byte) 2);
                builder.setServicePackMajor(servicePackMajor);

                /* service pack minor */
                errorOffset = buffer.bytesRead();
                int servicePackMinor = buffer.readInt((byte) 2);
                builder.setServicePackMinor(servicePackMinor);

            } catch (BufferUnderflowException e) {
                throw new SerializationException("Data length "
                        + buffer.bytesWritten() + " in buffer to short.", e,
                        true, Long.toString(buffer.bytesWritten()));
            }

            value = (PaAttributeValueNumericVersion) builder.toObject();

        } catch (RuleException e) {
            throw new ValidationException(e.getMessage(), e, errorOffset);
        }

        return value;
    }

    @Override
    public byte getMinDataLength() {
        return PaAttributeTlvFixedLengthEnum.NUM_VER.length();
    }

}
