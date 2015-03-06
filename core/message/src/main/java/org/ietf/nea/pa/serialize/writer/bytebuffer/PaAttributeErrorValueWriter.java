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
