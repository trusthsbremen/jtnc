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

import org.ietf.nea.pa.attribute.PaAttributeValuePortFilter;
import org.ietf.nea.pa.attribute.PaAttributeValuePortFilterBuilder;
import org.ietf.nea.pa.attribute.enums.PaAttributePortFilterStatus;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;
import org.ietf.nea.pa.attribute.util.PortFilterEntry;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

/**
 * Reader to parse an integrity measurement port filter attribute value
 * compliant to RFC 5792 from a buffer of bytes to a Java object.
 *
 * @author Carl-Heinz Genzel
 *
 */
class PaAttributePortFilterValueReader implements
        ImReader<PaAttributeValuePortFilter> {

    private PaAttributeValuePortFilterBuilder baseBuilder;

    /**
     * Creates the reader with a corresponding builder to validate the parsed
     * values and build the attribute value.
     *
     * @param builder the corresponding attribute value builder
     */
    PaAttributePortFilterValueReader(
            final PaAttributeValuePortFilterBuilder builder) {
        this.baseBuilder = builder;
    }

    @Override
    public PaAttributeValuePortFilter read(final ByteBuffer buffer,
            final long messageLength) throws SerializationException,
            ValidationException {

        long errorOffset = 0;

        PaAttributeValuePortFilter value = null;
        PaAttributeValuePortFilterBuilder builder =
                (PaAttributeValuePortFilterBuilder) this.baseBuilder
                .newInstance();

        try {

            try {

                long counter = 0;
                do {
                    // TODO error offset is vague because it cannot be
                    // calculated to the exact offset.
                    errorOffset = buffer.bytesRead();

                    /* attribute values */
                    PaAttributePortFilterStatus blocked;
                    short protocol;
                    int port;

                    /* ignore reserved but get blocked */
                    byte bit = buffer.readByte();
                    blocked = PaAttributePortFilterStatus
                            .fromBlockedBit((byte) (bit & 0x01));
                    counter++;

                    /* protocol */
                    protocol = buffer.readShort((byte) 1);
                    counter++;

                    /* port number */
                    port = buffer.readInt((byte) 2);
                    counter += 2;

                    builder.addEntries(new PortFilterEntry(blocked, protocol,
                            port));

                } while (messageLength - counter > 0);

            } catch (BufferUnderflowException e) {
                throw new SerializationException("Data length "
                        + buffer.bytesWritten() + " in buffer to short.", e,
                        true, Long.toString(buffer.bytesWritten()));
            }

            value = (PaAttributeValuePortFilter) builder.toObject();

        } catch (RuleException e) {
            throw new ValidationException(e.getMessage(), e, errorOffset);
        }

        return value;
    }

    @Override
    public byte getMinDataLength() {

        return PaAttributeTlvFixedLengthEnum.PORT_FT.length();
    }
}
