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
package org.ietf.nea.pb.serialize.writer.bytebuffer;

import java.nio.BufferOverflowException;
import java.util.Set;

import org.ietf.nea.pb.message.PbMessageValueError;
import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.message.enums.PbMessageErrorFlagsEnum;
import org.ietf.nea.pb.message.util.PbMessageValueErrorParameterOffset;
import org.ietf.nea.pb.message.util.PbMessageValueErrorParameterVersion;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.tnccs.serialize.bytebuffer.TnccsWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.util.NotNull;

/**
 * Writer to serialize a TNCCS error message value compliant to RFC 5793 from a
 * Java object to a buffer of bytes.
 *
 *
 */
class PbMessageErrorValueWriter implements TnccsWriter<PbMessageValueError> {

    private static final short RESERVED = 0;

    // TODO should be a map to make the error parameters more customizable
    private final PbMessageErrorParameterOffsetValueWriter offsetWriter;
    private final PbMessageErrorParameterVersionValueWriter versionWriter;

    /**
     * Creates the writer with the given writers, that are responsible to
     * serialize the contained supporting error parameter.
     *
     * @param offsetWriter the writer to serialize an error parameter containing
     * an error offset.
     * @param versionWriter the writer to serialize a unsupported version error
     * parameter
     */
    PbMessageErrorValueWriter(
            final PbMessageErrorParameterOffsetValueWriter offsetWriter,
            final PbMessageErrorParameterVersionValueWriter versionWriter) {
        this.offsetWriter = offsetWriter;
        this.versionWriter = versionWriter;
    }

    @Override
    public void write(final PbMessageValueError data, final ByteBuffer buffer)
            throws SerializationException {
        NotNull.check("Message value cannot be null.", data);

        PbMessageValueError mValue = data;

        try {

            /* flags 8 bit(s) */
            Set<PbMessageErrorFlagsEnum> flags = mValue.getErrorFlags();
            byte bFlags = 0;
            for (PbMessageErrorFlagsEnum pbMessageErrorFlagsEnum : flags) {
                bFlags |= pbMessageErrorFlagsEnum.bit();
            }
            buffer.writeByte(bFlags);

            /* vendor ID 24 bit(s) */
            buffer.writeDigits(mValue.getErrorVendorId(), (byte) 3);

            /* error Code */
            buffer.writeUnsignedShort(mValue.getErrorCode());

            /* reserved */
            buffer.writeShort(RESERVED);

        } catch (BufferOverflowException e) {
            throw new SerializationException("Buffer capacity "
                    + buffer.capacity() + " to short.", e, false,
                    Long.toString(buffer.capacity()));
        }

        /* error parameter */
        long errorVendor = mValue.getErrorVendorId();
        long errorCode = mValue.getErrorCode();

        if (errorVendor == IETFConstants.IETF_PEN_VENDORID) {
            if (errorCode == PbMessageErrorCodeEnum
                    .IETF_INVALID_PARAMETER.code()
                    || errorCode == PbMessageErrorCodeEnum
                        .IETF_UNSUPPORTED_MANDATORY_MESSAGE.code()) {

                this.offsetWriter.write(
                        (PbMessageValueErrorParameterOffset) mValue
                                .getErrorParameter(), buffer);

            } else if (errorCode == PbMessageErrorCodeEnum
                        .IETF_UNSUPPORTED_VERSION.code()) {

                this.versionWriter.write(
                        (PbMessageValueErrorParameterVersion) mValue
                                .getErrorParameter(), buffer);

            } else if (errorCode != PbMessageErrorCodeEnum.IETF_LOCAL.code()
                    && errorCode != PbMessageErrorCodeEnum
                                        .IETF_UNEXPECTED_BATCH_TYPE.code()) {

                throw new SerializationException(
                        "Error message type is not supported.", false,
                        Long.toString(errorVendor), Long.toString(errorCode));
            }
        } else {
            throw new SerializationException(
                    "Error vendor ID is not supported.", false,
                    Long.toString(errorVendor), Long.toString(errorCode));
        }
    }

}
