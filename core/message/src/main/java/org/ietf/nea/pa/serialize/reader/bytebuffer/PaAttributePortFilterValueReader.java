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
