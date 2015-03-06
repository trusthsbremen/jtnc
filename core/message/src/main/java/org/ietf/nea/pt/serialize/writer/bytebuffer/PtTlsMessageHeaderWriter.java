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
package org.ietf.nea.pt.serialize.writer.bytebuffer;

import java.nio.BufferOverflowException;

import org.ietf.nea.pt.message.PtTlsMessageHeader;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.t.serialize.bytebuffer.TransportWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.util.NotNull;

/**
 * Writer to serialize a transport message header compliant to RFC 6876 from a
 * Java object to a buffer of bytes.
 *
 *
 */
class PtTlsMessageHeaderWriter implements TransportWriter<PtTlsMessageHeader> {

    @Override
    public void write(final PtTlsMessageHeader data,
            final ByteBuffer buffer) throws SerializationException {
        NotNull.check("Message header cannot be null.", data);

        NotNull.check("Buffer cannot be null.", buffer);

        PtTlsMessageHeader mHead = data;

        try {

            /* reserved 8 bit(s) */
            buffer.writeByte((byte) 0);

            /* vendor ID 24 bit(s) */
            buffer.writeDigits(mHead.getVendorId(), (byte) 3);

            /* message Type 32 bit(s) */
            buffer.writeUnsignedInt(mHead.getMessageType());

            /* message length 32 bit(s) */
            buffer.writeUnsignedInt(mHead.getLength());

            /* message identifier 32 bit(s) */
            buffer.writeUnsignedInt(mHead.getIdentifier());

        } catch (BufferOverflowException e) {
            throw new SerializationException("Buffer capacity "
                    + buffer.capacity() + " to short.", e, false,
                    Long.toString(buffer.capacity()));
        }

    }

}
