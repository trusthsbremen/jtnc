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
import java.util.Set;

import org.ietf.nea.pa.attribute.PaAttributeHeader;
import org.ietf.nea.pa.attribute.enums.PaAttributeFlagEnum;
import org.ietf.nea.pa.attribute.util
.PaAttributeValueErrorInformationUnsupportedAttribute;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.util.NotNull;

/**
 * Writer to serialize an integrity measurement unsupported attribute error
 * information compliant to RFC 5792 from a Java object to a buffer of bytes.
 *
 *
 */
class PaAttributeErrorInformationUnsupportedAttributeValueWriter implements
        ImWriter<PaAttributeValueErrorInformationUnsupportedAttribute> {

    @Override
    public void write(
            final PaAttributeValueErrorInformationUnsupportedAttribute data,
            final ByteBuffer buffer) throws SerializationException {
        NotNull.check("Value cannot be null.", data);

        NotNull.check("Buffer cannot be null.", buffer);

        PaAttributeValueErrorInformationUnsupportedAttribute aValue = data;

        try {
            /* message header copy 64 bit(s) */
            /* copy version 8 bit(s) */
            buffer.writeUnsignedByte(data.getMessageHeader().getVersion());

            /* copy reserved 24 bit(s) */
            buffer.write(data.getMessageHeader().getReserved());

            /* copy identifier 32 bit(s) */
            buffer.writeUnsignedInt(data.getMessageHeader().getIdentifier());

            /* attribute header copy 64 bit(s) */
            PaAttributeHeader aHead = aValue.getAttributeHeader();

            /* flags 8 bit(s) */
            Set<PaAttributeFlagEnum> flags = aHead.getFlags();
            byte bFlags = 0;
            for (PaAttributeFlagEnum paAttributeFlagsEnum : flags) {
                bFlags |= paAttributeFlagsEnum.bit();
            }
            buffer.writeByte(bFlags);

            /* vendor ID 24 bit(s) */
            buffer.writeDigits(aHead.getVendorId(), (byte) 3);

            /* attribute Type 32 bit(s) */
            buffer.writeUnsignedInt(aHead.getAttributeType());

        } catch (BufferOverflowException e) {
            throw new SerializationException("Buffer capacity "
                    + buffer.capacity() + " to short.", e, false,
                    Long.toString(buffer.capacity()));
        }
    }

}
