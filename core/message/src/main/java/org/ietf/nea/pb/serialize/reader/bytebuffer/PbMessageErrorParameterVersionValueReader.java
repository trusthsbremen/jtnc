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

import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLengthEnum;
import org.ietf.nea.pb.message.util.PbMessageValueErrorParameterVersion;
import org.ietf.nea.pb.message.util.PbMessageValueErrorParameterVersionBuilder;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.serialize.bytebuffer.TnccsReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.util.NotNull;

/**
 * Reader to parse a TNCCS unsupported version error
 * parameter compliant to RFC 5793 from a buffer of bytes to a Java object.
 *
 * @author Carl-Heinz Genzel
 *
 */
class PbMessageErrorParameterVersionValueReader implements
        TnccsReader<PbMessageValueErrorParameterVersion> {

    private PbMessageValueErrorParameterVersionBuilder baseBuilder;

    /**
     * Creates the reader with a corresponding builder to validate the parsed
     * values and build the parameter.
     *
     * @param builder the corresponding parameter builder
     */
    PbMessageErrorParameterVersionValueReader(
            final PbMessageValueErrorParameterVersionBuilder builder) {
        this.baseBuilder = builder;
    }

    @Override
    public PbMessageValueErrorParameterVersion read(final ByteBuffer buffer,
            final long messageLength) throws SerializationException,
            ValidationException {

        NotNull.check("Buffer cannot be null.", buffer);

        long errorOffset = 0;

        PbMessageValueErrorParameterVersion value = null;
        PbMessageValueErrorParameterVersionBuilder builder =
                (PbMessageValueErrorParameterVersionBuilder) this.baseBuilder
                .newInstance();

        try {

            try {

                /* bad version 8 bit(s) */
                errorOffset = buffer.bytesRead();
                short badVersion = buffer.readShort((byte) 1);
                builder.setBadVersion(badVersion);

                /* max version 8 bit(s) */
                errorOffset = buffer.bytesRead();
                short maxVersion = buffer.readShort((byte) 1);
                builder.setMaxVersion(maxVersion);

                /* min version 8 bit(s) */
                errorOffset = buffer.bytesRead();
                short minVersion = buffer.readShort((byte) 1);
                builder.setMinVersion(minVersion);

                /* ignore reserved 8 bit(s) */
                errorOffset = buffer.bytesRead();
                buffer.readByte();

            } catch (BufferUnderflowException e) {
                throw new SerializationException("Data length "
                        + buffer.bytesWritten() + " in buffer to short.", e,
                        true, Long.toString(buffer.bytesWritten()));
            }

            value = (PbMessageValueErrorParameterVersion) builder.toObject();

        } catch (RuleException e) {
            throw new ValidationException(e.getMessage(), e, errorOffset);
        }

        return value;
    }

    @Override
    public byte getMinDataLength() {

        return PbMessageTlvFixedLengthEnum.ERR_SUB_VALUE.length();
    }
}
