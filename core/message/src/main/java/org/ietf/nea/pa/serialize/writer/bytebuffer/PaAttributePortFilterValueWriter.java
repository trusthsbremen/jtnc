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
package org.ietf.nea.pa.serialize.writer.bytebuffer;

import java.nio.BufferOverflowException;
import java.util.List;

import org.ietf.nea.pa.attribute.PaAttributeValuePortFilter;
import org.ietf.nea.pa.attribute.util.PortFilterEntry;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.util.NotNull;

/**
 * Writer to serialize an integrity measurement port filter attribute value
 * compliant to RFC 5792 from a Java object to a buffer of bytes.
 *
 * @author Carl-Heinz Genzel
 *
 */
class PaAttributePortFilterValueWriter implements
        ImWriter<PaAttributeValuePortFilter> {

    private static final byte RESERVED = 0;

    @Override
    public void write(final PaAttributeValuePortFilter data,
            final ByteBuffer buffer) throws SerializationException {
        NotNull.check("Value cannot be null.", data);

        NotNull.check("Buffer cannot be null.", buffer);

        PaAttributeValuePortFilter aValue = data;

        try {

            List<PortFilterEntry> entries = aValue.getFilterEntries();
            if (entries != null && entries.size() > 0) {
                for (PortFilterEntry entry : entries) {

                    /*
                     * reserved (7 bit(s)) + blocking status (1 bit(s)) = 8
                     * bit(s)
                     */
                    buffer.writeByte((byte) (RESERVED | (entry
                            .getFilterStatus().toStatusBit() & 0x01)));

                    /* protocol 8 bit(s) */
                    buffer.writeUnsignedByte(entry.getProtocolNumber());

                    /* port number 16 bit(s) */
                    buffer.writeUnsignedShort(entry.getProtocolNumber());

                }
            } else {
                throw new SerializationException(
                        "No port filter entry available, "
                        + "but there must be at least one.",
                        false);
            }

        } catch (BufferOverflowException e) {
            throw new SerializationException("Buffer capacity "
                    + buffer.capacity() + " to short.", e, false,
                    Long.toString(buffer.capacity()));
        }
    }

}
