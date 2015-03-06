/**
 * The BSD 3-Clause License ("BSD New" or "BSD Simplified")
 *
 * Copyright (c) 2015, Carl-Heinz Genzel
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER
 * OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
