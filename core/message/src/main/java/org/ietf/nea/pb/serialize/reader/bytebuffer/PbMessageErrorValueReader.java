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

import org.ietf.nea.pb.message.PbMessageValueError;
import org.ietf.nea.pb.message.PbMessageValueErrorBuilder;
import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLengthEnum;
import org.ietf.nea.pb.message.util.PbMessageValueErrorParameterOffset;
import org.ietf.nea.pb.message.util.PbMessageValueErrorParameterVersion;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.serialize.bytebuffer.TnccsReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.util.NotNull;

/**
 * Reader to parse a TNCCS error message value compliant to RFC 5793 from a
 * buffer of bytes to a Java object.
 *
 *
 */
class PbMessageErrorValueReader implements TnccsReader<PbMessageValueError> {

    private PbMessageValueErrorBuilder baseBuilder;

    // TODO should be a map to make the error parameters more customizable
    private final PbMessageErrorParameterOffsetValueReader offsetReader;
    private final PbMessageErrorParameterVersionValueReader versionReader;

    /**
     * Creates the reader with a corresponding builder to validate the parsed
     * values and build the message value. The given readers are responsible to
     * parse the contained supporting error parameter.
     *
     * @param builder the corresponding message value builder
     * @param offsetReader the reader to parse an error parameter containing an
     * error offset.
     * @param versionReader the reader to parse a unsupported version error
     * parameter
     */
    PbMessageErrorValueReader(final PbMessageValueErrorBuilder builder,
            final PbMessageErrorParameterOffsetValueReader offsetReader,
            final PbMessageErrorParameterVersionValueReader versionReader) {
        this.baseBuilder = builder;
        this.offsetReader = offsetReader;
        this.versionReader = versionReader;
    }

    @Override
    public PbMessageValueError read(final ByteBuffer buffer,
            final long messageLength) throws SerializationException,
            ValidationException {

        NotNull.check("Buffer cannot be null.", buffer);

        long errorOffset = 0;

        PbMessageValueError value = null;
        PbMessageValueErrorBuilder builder =
                (PbMessageValueErrorBuilder) this.baseBuilder.newInstance();

        long errorVendorId = 0L;
        int errorCode = 0;

        try {

            try {

                /* flags 8 bit(s) */
                errorOffset = buffer.bytesRead();
                builder.setErrorFlags(buffer.readByte());

                /* vendor ID 24 bit(s) */
                errorOffset = buffer.bytesRead();
                errorVendorId = buffer.readLong((byte) 3);
                builder.setErrorVendorId(errorVendorId);

                /* error Code 16 bit(s) */
                errorOffset = buffer.bytesRead();
                errorCode = buffer.readInt((byte) 2);
                builder.setErrorCode(errorCode);

                /* ignore Reserved 16 bit(s) */
                errorOffset = buffer.bytesRead();
                buffer.read(2);

            } catch (BufferUnderflowException e) {
                throw new SerializationException("Data length "
                        + buffer.bytesWritten() + " in buffer to short.", e,
                        true, Long.toString(buffer.bytesWritten()));
            }

            /* error parameter */
            // value length = overall message length - header length
            long valueLength = messageLength - this.getMinDataLength();

            if (errorCode == PbMessageErrorCodeEnum
                        .IETF_INVALID_PARAMETER.code()
                    || errorCode == PbMessageErrorCodeEnum
                        .IETF_UNSUPPORTED_MANDATORY_MESSAGE.code()) {

                PbMessageValueErrorParameterOffset param = this.offsetReader
                        .read(buffer, valueLength);
                builder.setErrorParameter(param);

            } else if (errorCode == PbMessageErrorCodeEnum
                    .IETF_UNSUPPORTED_VERSION.code()) {

                PbMessageValueErrorParameterVersion param = this.versionReader
                        .read(buffer, valueLength);
                builder.setErrorParameter(param);

            } else if (errorCode != PbMessageErrorCodeEnum.IETF_LOCAL.code()
                    && errorCode != PbMessageErrorCodeEnum
                        .IETF_UNEXPECTED_BATCH_TYPE.code()) {
                try {
                    // skip the remaining bytes of the message
                    buffer.read(valueLength);
                } catch (BufferUnderflowException e) {
                    throw new SerializationException("Data length "
                            + buffer.bytesWritten() + " in buffer to short.",
                            e, true, Long.toString(buffer.bytesWritten()));
                }
                // TODO make a default error object?
                return null;
            }

            value = (PbMessageValueError) builder.toObject();

        } catch (RuleException e) {
            throw new ValidationException(e.getMessage(), e, errorOffset);
        }

        return value;
    }

    @Override
    public byte getMinDataLength() {
        return PbMessageTlvFixedLengthEnum.ERR_VALUE.length();
    }

}
