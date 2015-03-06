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

import org.ietf.nea.pa.attribute.PaAttributeValueRemediationParameters;
import org.ietf.nea.pa.attribute.enums.PaAttributeRemediationParameterTypeEnum;
import org.ietf.nea.pa.attribute.util
.PaAttributeValueRemediationParameterString;
import org.ietf.nea.pa.attribute.util.PaAttributeValueRemediationParameterUri;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.util.NotNull;

/**
 * Writer to serialize an integrity measurement remediation parameters attribute
 * value compliant to RFC 5792 from a Java object to a buffer of bytes.
 *
 *
 */
class PaAttributeRemediationParametersValueWriter implements
        ImWriter<PaAttributeValueRemediationParameters> {

    private static final byte RESERVED = 0;

    // TODO should be a map to make the remediation parameters more customizable
    private final PaAttributeRemediationParameterStringValueWriter stringWriter;
    private final PaAttributeRemediationParameterUriValueWriter uriWriter;

    /**
     * Creates the writer with the given writers that are responsible to
     * serialize the contained supporting remediation parameter.
     *
     * @param stringWriter the writer to serialize a string remediation
     * parameter
     * @param uriWriter the writer to serialize a URI remediation parameter
     */
    PaAttributeRemediationParametersValueWriter(
            final PaAttributeRemediationParameterStringValueWriter stringWriter,
            final PaAttributeRemediationParameterUriValueWriter uriWriter) {
        this.stringWriter = stringWriter;
        this.uriWriter = uriWriter;
    }

    @Override
    public void write(final PaAttributeValueRemediationParameters data,
            final ByteBuffer buffer) throws SerializationException {
        NotNull.check("Value cannot be null.", data);

        NotNull.check("Buffer cannot be null.", buffer);

        PaAttributeValueRemediationParameters mValue = data;

        try {
            /* reserved 8 bit(s) */
            buffer.writeByte(RESERVED);

            /* vendor ID 24 bit(s) */
            buffer.writeDigits(mValue.getRpVendorId(), (byte) 3);

            /* remediation type 32 bit(s) */
            buffer.writeUnsignedInt(mValue.getRpType());

        } catch (BufferOverflowException e) {
            throw new SerializationException("Buffer capacity "
                    + buffer.capacity() + " to short.", e, false,
                    Long.toString(buffer.capacity()));
        }

        /* remediation parameter */
        long rpVendor = mValue.getRpVendorId();
        long rpMessageType = mValue.getRpType();

        if (rpVendor == IETFConstants.IETF_PEN_VENDORID) {
            if (rpMessageType == PaAttributeRemediationParameterTypeEnum
                    .IETF_STRING.id()) {
                this.stringWriter.write(
                        (PaAttributeValueRemediationParameterString) mValue
                                .getParameter(), buffer);

            } else if (rpMessageType == PaAttributeRemediationParameterTypeEnum
                    .IETF_URI.id()) {
                this.uriWriter.write(
                        (PaAttributeValueRemediationParameterUri) mValue
                                .getParameter(), buffer);
            } else {
                throw new SerializationException(
                        "Remediation message type is not supported.", false,
                        Long.toString(rpVendor), Long.toString(rpMessageType));
            }
        } else {
            throw new SerializationException(
                    "Remediation vendor ID is not supported.", false,
                    Long.toString(rpVendor), Long.toString(rpMessageType));
        }
    }

}
