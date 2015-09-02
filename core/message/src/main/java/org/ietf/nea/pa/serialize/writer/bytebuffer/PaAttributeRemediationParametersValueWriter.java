/**
 * The BSD 3-Clause License ("BSD New" or "BSD Simplified")
 *
 * Copyright © 2015 Trust HS Bremen and its Contributors. All rights   
 * reserved.
 *
 * See the CONTRIBUTORS file distributed with this work for additional
 * information regarding copyright ownership.
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
     * Creates the writer with the given writers, that are responsible to
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
