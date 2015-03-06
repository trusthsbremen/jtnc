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
 * @author Carl-Heinz Genzel
 *
 */
class PbMessageErrorValueWriter implements TnccsWriter<PbMessageValueError> {

    private static final short RESERVED = 0;

    // TODO should be a map to make the error parameters more customizable
    private final PbMessageErrorParameterOffsetValueWriter offsetWriter;
    private final PbMessageErrorParameterVersionValueWriter versionWriter;

    /**
     * Creates the writer with the given writers that are responsible to
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
