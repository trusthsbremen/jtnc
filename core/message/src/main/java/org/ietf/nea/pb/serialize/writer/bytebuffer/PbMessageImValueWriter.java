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
import java.util.Set;

import org.ietf.nea.pb.message.PbMessageValueIm;
import org.ietf.nea.pb.message.enums.PbMessageImFlagEnum;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.tnccs.serialize.bytebuffer.TnccsWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.util.NotNull;

/**
 * Writer to serialize a TNCCS integrity measurement component message value
 * compliant to RFC 5793 from a Java object to a buffer of bytes.
 *
 *
 */
class PbMessageImValueWriter implements TnccsWriter<PbMessageValueIm> {

    @Override
    public void write(final PbMessageValueIm data, final ByteBuffer buffer)
            throws SerializationException {
        NotNull.check("Message value cannot be null.", data);

        PbMessageValueIm mValue = data;

        try {
            /* flags 8 bit(s) */
            Set<PbMessageImFlagEnum> flags = mValue.getImFlags();
            byte bFlags = 0;
            for (PbMessageImFlagEnum pbMessageImFlagsEnum : flags) {
                bFlags |= pbMessageImFlagsEnum.bit();
            }
            buffer.writeByte(bFlags);

            /* vendor ID 24 bit(s) */
            buffer.writeDigits(mValue.getSubVendorId(), (byte) 3);

            /* message type 32 bit(s) */
            buffer.writeUnsignedInt(mValue.getSubType());

            /* collector ID */
            buffer.writeDigits(mValue.getCollectorId(), (byte) 2);

            /* validator ID */
            buffer.writeDigits(mValue.getValidatorId(), (byte) 2);

            /* im message */
            buffer.write((mValue.getMessage() != null) ? mValue.getMessage()
                    : new byte[0]);

        } catch (BufferOverflowException e) {
            throw new SerializationException("Buffer capacity "
                    + buffer.capacity() + " to short.", e, false,
                    Long.toString(buffer.capacity()));
        }
    }

}
