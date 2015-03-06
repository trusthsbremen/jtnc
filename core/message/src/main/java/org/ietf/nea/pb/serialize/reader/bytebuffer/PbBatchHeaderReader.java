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
package org.ietf.nea.pb.serialize.reader.bytebuffer;

import java.nio.BufferUnderflowException;

import org.ietf.nea.pb.batch.PbBatchHeader;
import org.ietf.nea.pb.batch.PbBatchHeaderBuilder;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLengthEnum;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.serialize.bytebuffer.TnccsReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.util.NotNull;

/**
 * Reader to parse a TNCCS batch header compliant to RFC 5793 from a buffer of
 * bytes to a Java object.
 *
 *
 */
class PbBatchHeaderReader implements TnccsReader<PbBatchHeader> {

    private PbBatchHeaderBuilder baseBuilder;

    /**
     * Creates the reader with a corresponding builder to validate the parsed
     * values and build the batch header.
     *
     * @param builder the corresponding batch header builder
     */
    PbBatchHeaderReader(final PbBatchHeaderBuilder builder) {
        this.baseBuilder = builder;
    }

    @Override
    public PbBatchHeader read(final ByteBuffer buffer, final long batchLength)
            throws SerializationException, ValidationException {

        NotNull.check("Buffer cannot be null.", buffer);
        // ignore any given length and find out on your own.

        long errorOffset = 0;

        PbBatchHeader batchHeader = null;
        PbBatchHeaderBuilder builder = (PbBatchHeaderBuilder) this.baseBuilder
                .newInstance();

        try {
            try {

                /* version 8 bit(s) */
                errorOffset = buffer.bytesRead();
                builder.setVersion(buffer.readByte());

                /* direction 1 bit(s) of byte direction + reserved */
                errorOffset = buffer.bytesRead();
                byte directionality = (byte) ((buffer.readByte() & 0x80) >>> 7);
                builder.setDirection(directionality);

                /* ignore reserved 8 bit(s) */
                errorOffset = buffer.bytesRead();
                buffer.readByte();

                /* type 4 bit(s) of byte reserved + type */
                errorOffset = buffer.bytesRead();
                byte type = (byte) (buffer.readByte() & 0x0F);
                builder.setType(type);

                /* length 32 bit(s) */
                errorOffset = buffer.bytesRead();
                long length = buffer.readLong((byte) 4);
                builder.setLength(length);

            } catch (BufferUnderflowException e) {
                throw new SerializationException("Data length "
                        + buffer.bytesWritten() + " in buffer to short.", e,
                        true, Long.toString(buffer.bytesWritten()));
            }

            batchHeader = (PbBatchHeader) builder.toObject();

        } catch (RuleException e) {
            throw new ValidationException(e.getMessage(), e, errorOffset);
        }

        return batchHeader;
    }

    @Override
    public byte getMinDataLength() {
        return PbMessageTlvFixedLengthEnum.BATCH.length();
    }

}
