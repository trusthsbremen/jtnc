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
package org.ietf.nea.pb.serialize.writer.bytebuffer;

import java.nio.BufferOverflowException;

import org.ietf.nea.pb.batch.PbBatchHeader;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.tnccs.serialize.bytebuffer.TnccsWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.util.NotNull;

/**
 * Writer to serialize a TNCCS batch header compliant to RFC 5793 from a Java
 * object to a buffer of bytes.
 *
 * @author Carl-Heinz Genzel
 *
 */
class PbBatchHeaderWriter implements TnccsWriter<PbBatchHeader> {

    private static final byte RESERVED = 0;

    @Override
    public void write(final PbBatchHeader data, final ByteBuffer buffer)
            throws SerializationException {
        NotNull.check("Batch header cannot be null.", data);

        PbBatchHeader bHead = data;

        try {
            /* Version 8 bit(s) */
            buffer.writeUnsignedByte(bHead.getVersion());

            /* Direction 1 bit(s) + Reserved 7 bit(s) */
            buffer.writeByte((byte) (bHead.getDirectionality()
                    .toDirectionalityBit() << 7 | RESERVED));

            /* Reserved 8 bit(s) */
            buffer.writeByte(RESERVED);

            /* Reserved 4 bit(s) + Type 4 bit(s) */
            buffer.writeByte((byte) (RESERVED | (bHead.getType().id() & 0x0F)));

            /* Length 32 bit(s) */
            buffer.writeUnsignedInt(bHead.getLength());

        } catch (BufferOverflowException e) {
            throw new SerializationException("Buffer capacity "
                    + buffer.capacity() + " to short.", e, false,
                    Long.toString(buffer.capacity()));
        }

    }

}
