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
