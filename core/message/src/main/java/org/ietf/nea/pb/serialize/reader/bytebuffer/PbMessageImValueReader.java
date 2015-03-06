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

import org.ietf.nea.pb.message.PbMessageValueIm;
import org.ietf.nea.pb.message.PbMessageValueImBuilder;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLengthEnum;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.serialize.bytebuffer.TnccsReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.util.NotNull;

/**
 * Reader to parse a TNCCS integrity measurement component message value
 * compliant to RFC 5793 from a buffer of bytes to a Java object.
 *
 *
 */
class PbMessageImValueReader implements TnccsReader<PbMessageValueIm> {

    private PbMessageValueImBuilder baseBuilder;

    /**
     * Creates the reader with a corresponding builder to validate the parsed
     * values and build the message value.
     *
     * @param builder the corresponding message value builder
     */
    PbMessageImValueReader(final PbMessageValueImBuilder builder) {
        this.baseBuilder = builder;
    }

    @Override
    public PbMessageValueIm read(final ByteBuffer buffer,
            final long messageLength) throws SerializationException,
            ValidationException {

        NotNull.check("Buffer cannot be null.", buffer);

        long errorOffset = 0;

        PbMessageValueIm value = null;
        PbMessageValueImBuilder builder =
                (PbMessageValueImBuilder) this.baseBuilder
                .newInstance();

        try {

            try {

                /* flags 8 bit(s) */
                errorOffset = buffer.bytesRead();
                builder.setImFlags(buffer.readByte());

                /* sub vendor ID 24 bit(s) */
                errorOffset = buffer.bytesRead();
                long subVendorId = buffer.readLong((byte) 3);
                builder.setSubVendorId(subVendorId);

                /* sub message type 32 bit(s) */
                errorOffset = buffer.bytesRead();
                long subType = buffer.readLong((byte) 4);
                builder.setSubType(subType);

                /* collector ID 16 bit(s) */
                errorOffset = buffer.bytesRead();
                long collectorId = buffer.readLong((byte) 2);
                builder.setCollectorId(collectorId);

                /* validator ID 16 bit(s) */
                errorOffset = buffer.bytesRead();
                long validatorId = buffer.readLong((byte) 2);
                builder.setValidatorId(validatorId);

                /* PA message */
                errorOffset = buffer.bytesRead();
                /* FIXME: the length may be shortened here, to respect the
                 * maximum Java array length.
                 */
                long valueLength = messageLength
                        - this.getMinDataLength();
                int safeLength = (valueLength <= Integer.MAX_VALUE)
                ? (int) valueLength : Integer.MAX_VALUE;

                byte[] imMessage = buffer.read((int) (safeLength));
                if (valueLength > safeLength) {
                    // skip the rest that does not fit
                    buffer.read(valueLength - safeLength);
                }
                builder.setMessage(imMessage);

            } catch (BufferUnderflowException e) {
                throw new SerializationException("Data length "
                        + buffer.bytesWritten() + " in buffer to short.", e,
                        true, Long.toString(buffer.bytesWritten()));
            }

            value = (PbMessageValueIm) builder.toObject();

        } catch (RuleException e) {
            throw new ValidationException(e.getMessage(), e, errorOffset);
        }

        return value;
    }

    @Override
    public byte getMinDataLength() {
        return PbMessageTlvFixedLengthEnum.IM_VALUE.length();
    }

}
