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
import java.nio.charset.Charset;

import org.ietf.nea.pa.attribute.PaAttributeValueOperationalStatus;
import org.ietf.nea.pa.attribute.PaAttributeValueOperationalStatusBuilder;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

/**
 * Reader to parse an integrity measurement operational status attribute value
 * compliant to RFC 5792 from a buffer of bytes to a Java object.
 *
 *
 */
class PaAttributeOperationalStatusValueReader implements
        ImReader<PaAttributeValueOperationalStatus> {

    private PaAttributeValueOperationalStatusBuilder baseBuilder;

    /**
     * Creates the reader with a corresponding builder to validate the parsed
     * values and build the attribute value.
     *
     * @param builder the corresponding attribute value builder
     */
    PaAttributeOperationalStatusValueReader(
            final PaAttributeValueOperationalStatusBuilder builder) {
        this.baseBuilder = builder;
    }

    @Override
    public PaAttributeValueOperationalStatus read(final ByteBuffer buffer,
            final long messageLength) throws SerializationException,
            ValidationException {

        // ignore any given length and find out on your own.

        long errorOffset = 0;

        PaAttributeValueOperationalStatus value = null;
        PaAttributeValueOperationalStatusBuilder builder =
                (PaAttributeValueOperationalStatusBuilder) baseBuilder
                .newInstance();

        try {
            try {

                /* status */
                errorOffset = buffer.bytesRead();
                short status = buffer.readShort((byte) 1);
                builder.setStatus(status);

                /* result */
                errorOffset = buffer.bytesRead();
                short result = buffer.readShort((byte) 1);
                builder.setResult(result);

                /* ignore reserved */
                errorOffset = buffer.bytesRead();
                buffer.read(2);

                /* last use */
                errorOffset = buffer.bytesRead();
                byte[] sData = buffer.read(20);
                String lastUse = new String(sData, Charset.forName("US-ASCII"));
                builder.setLastUse(lastUse);

            } catch (BufferUnderflowException e) {
                throw new SerializationException("Data length "
                        + buffer.bytesWritten() + " in buffer to short.", e,
                        true, Long.toString(buffer.bytesWritten()));
            }

            value = (PaAttributeValueOperationalStatus) builder.toObject();

        } catch (RuleException e) {
            throw new ValidationException(e.getMessage(), e, errorOffset);
        }

        return value;
    }

    @Override
    public byte getMinDataLength() {
        return PaAttributeTlvFixedLengthEnum.OP_STAT.length();
    }

}
