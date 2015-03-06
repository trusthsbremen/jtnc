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
