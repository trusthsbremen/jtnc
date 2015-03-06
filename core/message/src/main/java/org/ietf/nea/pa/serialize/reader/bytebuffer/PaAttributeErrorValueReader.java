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
package org.ietf.nea.pa.serialize.reader.bytebuffer;

import java.nio.BufferUnderflowException;

import org.ietf.nea.pa.attribute.PaAttributeValueError;
import org.ietf.nea.pa.attribute.PaAttributeValueErrorBuilder;
import org.ietf.nea.pa.attribute.enums.PaAttributeErrorCodeEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;
import org.ietf.nea.pa.attribute.util
.PaAttributeValueErrorInformationInvalidParam;
import org.ietf.nea.pa.attribute.util
.PaAttributeValueErrorInformationUnsupportedAttribute;
import org.ietf.nea.pa.attribute.util
.PaAttributeValueErrorInformationUnsupportedVersion;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

/**
 * Reader to parse an integrity measurement error attribute value compliant to
 * RFC 5792 from a buffer of bytes to a Java object.
 *
 *
 */
class PaAttributeErrorValueReader implements ImReader<PaAttributeValueError> {

    private PaAttributeValueErrorBuilder baseBuilder;

    // TODO should be a map to make the error parameters more customizable
    private final PaAttributeErrorInformationInvalidParamValueReader
        invalidParamReader;
    private final PaAttributeErrorInformationUnsupportedVersionValueReader
        unsupportedVersionReader;
    private final PaAttributeErrorInformationUnsupportedAttributeValueReader
        unsupportedAttributeReader;

    /**
     * Creates the reader with a corresponding builder to validate the parsed
     * values and build the attribute value. The given readers are responsible
     * to parse the contained supporting error information.
     *
     * @param builder the corresponding attribute value builder
     * @param invalidParamReader the reader to parse an invalid parameter error
     * information
     * @param unsupportedVersionReader the reader to parse an unsupported
     * version error information
     * @param unsupportedAttributeReader the reader to parse an unsupported
     * attribute error information
     */
    public PaAttributeErrorValueReader(
            final PaAttributeValueErrorBuilder builder,
            final PaAttributeErrorInformationInvalidParamValueReader
                invalidParamReader,
            final PaAttributeErrorInformationUnsupportedVersionValueReader
                unsupportedVersionReader,
            final PaAttributeErrorInformationUnsupportedAttributeValueReader
                unsupportedAttributeReader) {
        this.baseBuilder = builder;
        this.invalidParamReader = invalidParamReader;
        this.unsupportedAttributeReader = unsupportedAttributeReader;
        this.unsupportedVersionReader = unsupportedVersionReader;
    }

    @Override
    public PaAttributeValueError read(final ByteBuffer buffer,
            final long messageLength) throws SerializationException,
            ValidationException {

        long errorOffset = 0;

        PaAttributeValueError value = null;
        PaAttributeValueErrorBuilder builder =
                (PaAttributeValueErrorBuilder) this.baseBuilder
                .newInstance();

        long errorVendorId = 0L;
        long errorCode = 0L;

        try {

            try {

                /* ignore reserved */
                errorOffset = buffer.bytesRead();
                buffer.readByte();

                /* vendor ID */
                errorOffset = buffer.bytesRead();
                errorVendorId = buffer.readLong((byte) 3);
                builder.setErrorVendorId(errorVendorId);

                /* code */
                errorOffset = buffer.bytesRead();
                errorCode = buffer.readLong((byte) 4);
                builder.setErrorCode(errorCode);

            } catch (BufferUnderflowException e) {
                throw new SerializationException("Data length "
                        + buffer.bytesWritten() + " in buffer to short.", e,
                        true, Long.toString(buffer.bytesWritten()));
            }

            /* error parameter */
            // value length = header length - overall message length
            long valueLength = messageLength
                    - PaAttributeTlvFixedLengthEnum.ERR_INF.length();

            try {
                if (errorCode == PaAttributeErrorCodeEnum.IETF_INVALID_PARAMETER
                        .code()) {

                    PaAttributeValueErrorInformationInvalidParam
                        errorInformation = this.invalidParamReader.read(
                                buffer, valueLength);
                    builder.setErrorInformation(errorInformation);

                } else if (errorCode == PaAttributeErrorCodeEnum
                            .IETF_UNSUPPORTED_VERSION.code()) {

                    PaAttributeValueErrorInformationUnsupportedVersion
                        errorInformation = this.unsupportedVersionReader.read(
                                buffer, valueLength);
                    builder.setErrorInformation(errorInformation);

                } else if (errorCode == PaAttributeErrorCodeEnum
                            .IETF_UNSUPPORTED_MANDATORY_ATTRIBUTE.code()) {

                    PaAttributeValueErrorInformationUnsupportedAttribute
                        errorInformation = this.unsupportedAttributeReader.read(
                                buffer, valueLength);
                    builder.setErrorInformation(errorInformation);

                } else {
                    try {
                        // skip the remaining bytes of the message
                        buffer.read(valueLength);
                    } catch (BufferUnderflowException e) {
                        throw new SerializationException("Data length "
                                + buffer.bytesWritten()
                                + " in buffer to short.", e, true,
                                Long.toString(buffer.bytesWritten()));
                    }
                    // TODO make a default error object?
                    return null;
                }
            } catch (ValidationException e) {
                // catch exception and add throw with recalculated offset, pass
                // on the rule exception
                throw new ValidationException(e.getMessage(), e.getCause(),
                        e.getExceptionOffset() + errorOffset);
            }

            value = (PaAttributeValueError) builder.toObject();

        } catch (RuleException e) {
            throw new ValidationException(e.getMessage(), e, errorOffset);
        }

        return value;
    }

    @Override
    public byte getMinDataLength() {
        return PaAttributeTlvFixedLengthEnum.ERR_INF.length();
    }

}
