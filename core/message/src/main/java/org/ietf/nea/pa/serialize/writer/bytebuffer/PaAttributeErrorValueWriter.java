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
package org.ietf.nea.pa.serialize.writer.bytebuffer;

import java.nio.BufferOverflowException;

import org.ietf.nea.pa.attribute.PaAttributeValueError;
import org.ietf.nea.pa.attribute.enums.PaAttributeErrorCodeEnum;
import org.ietf.nea.pa.attribute.util
.PaAttributeValueErrorInformationInvalidParam;
import org.ietf.nea.pa.attribute.util
.PaAttributeValueErrorInformationUnsupportedAttribute;
import org.ietf.nea.pa.attribute.util
.PaAttributeValueErrorInformationUnsupportedVersion;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.util.NotNull;

/**
 * Writer to serialize an integrity measurement error attribute value compliant
 * to RFC 5792 from a Java object to a buffer of bytes.
 *
 *
 */
class PaAttributeErrorValueWriter implements ImWriter<PaAttributeValueError> {

    private static final byte RESERVED = 0;

    // TODO should be a map to make the error parameters more customizable
    private final PaAttributeErrorInformationInvalidParamValueWriter
        invalidParamWriter;
    private final PaAttributeErrorInformationUnsupportedVersionValueWriter
        unsupportedVersionWriter;
    private final PaAttributeErrorInformationUnsupportedAttributeValueWriter
        unsupportedAttributeWriter;

    /**
     * Creates the writer with the given writers that are responsible to
     * serialize the contained supporting error information.
     *
     * @param invalidParamWriter the writer to serialize an invalid parameter
     * error information
     * @param unsupportedVersionWriter the writer to serialize an unsupported
     * version error information
     * @param unsupportedAttributeWriter the writer to serialize an unsupported
     * attribute error information
     */
    PaAttributeErrorValueWriter(
            final PaAttributeErrorInformationInvalidParamValueWriter
                invalidParamWriter,
            final PaAttributeErrorInformationUnsupportedVersionValueWriter
                unsupportedVersionWriter,
            final PaAttributeErrorInformationUnsupportedAttributeValueWriter
                unsupportedAttributeWriter) {

        this.invalidParamWriter = invalidParamWriter;
        this.unsupportedVersionWriter = unsupportedVersionWriter;
        this.unsupportedAttributeWriter = unsupportedAttributeWriter;
    }

    @Override
    public void write(final PaAttributeValueError data, final ByteBuffer buffer)
            throws SerializationException {
        NotNull.check("Value cannot be null.", data);

        NotNull.check("Buffer cannot be null.", buffer);

        PaAttributeValueError mValue = data;

        try {

            /* reserved 8 bit(s) */
            buffer.writeByte(RESERVED);

            /* vendor ID 24 bit(s) */
            buffer.writeDigits(mValue.getErrorVendorId(), (byte) 3);

            /* error code 32 bit(s) */
            buffer.writeUnsignedInt(mValue.getErrorCode());

        } catch (BufferOverflowException e) {
            throw new SerializationException("Buffer capacity "
                    + buffer.capacity() + " to short.", e, false,
                    Long.toString(buffer.capacity()));
        }

        /* error information */
        long errorVendor = mValue.getErrorVendorId();
        long errorCode = mValue.getErrorCode();

        if (errorVendor == IETFConstants.IETF_PEN_VENDORID) {
            if (errorCode == PaAttributeErrorCodeEnum.IETF_INVALID_PARAMETER
                    .code()) {

                this.invalidParamWriter.write(
                        (PaAttributeValueErrorInformationInvalidParam) mValue
                                .getErrorInformation(), buffer);

            } else if (errorCode == PaAttributeErrorCodeEnum
                        .IETF_UNSUPPORTED_VERSION.code()) {

                this.unsupportedVersionWriter.write(
                        (PaAttributeValueErrorInformationUnsupportedVersion)
                        mValue.getErrorInformation(), buffer);

            } else if (errorCode == PaAttributeErrorCodeEnum
                        .IETF_UNSUPPORTED_MANDATORY_ATTRIBUTE.code()) {

                this.unsupportedAttributeWriter.write(
                        (PaAttributeValueErrorInformationUnsupportedAttribute)
                        mValue.getErrorInformation(), buffer);

            } else {
                throw new SerializationException(
                        "Error code type is not supported.", false,
                        Long.toString(errorVendor), Long.toString(errorCode));
            }
        } else {
            throw new SerializationException(
                    "Error vendor ID is not supported.", false,
                    Long.toString(errorVendor), Long.toString(errorCode));
        }
    }

}
