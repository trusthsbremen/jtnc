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
package org.ietf.nea.pa.serialize.reader.bytebuffer;

import java.nio.BufferUnderflowException;

import org.ietf.nea.pa.attribute.PaAttributeValueRemediationParameters;
import org.ietf.nea.pa.attribute.PaAttributeValueRemediationParametersBuilder;
import org.ietf.nea.pa.attribute.enums.PaAttributeRemediationParameterTypeEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;
import org.ietf.nea.pa.attribute.util
.PaAttributeValueRemediationParameterString;
import org.ietf.nea.pa.attribute.util.PaAttributeValueRemediationParameterUri;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

/**
 * Reader to parse an integrity measurement remediation parameters attribute
 * value compliant to RFC 5792 from a buffer of bytes to a Java object.
 *
 *
 */
class PaAttributeRemediationParametersValueReader implements
        ImReader<PaAttributeValueRemediationParameters> {

    private PaAttributeValueRemediationParametersBuilder baseBuilder;

    // TODO should be a map to make the remediation parameters more customizable
    private final PaAttributeRemediationParameterStringValueReader stringReader;
    private final PaAttributeRemediationParameterUriValueReader uriReader;

    /**
     * Creates the reader with a corresponding builder to validate the parsed
     * values and build the attribute value. The given readers are responsible
     * to parse the contained supporting remediation parameter.
     *
     * @param builder the corresponding attribute value builder
     * @param stringReader the reader to parse a string remediation parameter
     * @param uriReader the reader to parse a URI remediation parameter
     */
    public PaAttributeRemediationParametersValueReader(
            final PaAttributeValueRemediationParametersBuilder builder,
            final PaAttributeRemediationParameterStringValueReader stringReader,
            final PaAttributeRemediationParameterUriValueReader uriReader) {
        this.baseBuilder = builder;
        this.stringReader = stringReader;
        this.uriReader = uriReader;
    }

    @Override
    public PaAttributeValueRemediationParameters read(final ByteBuffer buffer,
            final long messageLength) throws SerializationException,
            ValidationException {

        long errorOffset = 0;

        PaAttributeValueRemediationParameters value = null;
        PaAttributeValueRemediationParametersBuilder builder =
                (PaAttributeValueRemediationParametersBuilder) this.baseBuilder
                .newInstance();

        long rpVendorId = 0L;
        long rpType = 0L;

        try {

            try {

                /* ignore reserved */
                errorOffset = buffer.bytesRead();
                buffer.readByte();

                /* vendor ID */
                errorOffset = buffer.bytesRead();
                rpVendorId = buffer.readLong((byte) 3);
                builder.setRpVendorId(rpVendorId);

                /* remediation type */
                errorOffset = buffer.bytesRead();
                rpType = buffer.readLong((byte) 4);
                builder.setRpType(rpType);

            } catch (BufferUnderflowException e) {
                throw new SerializationException("Data length "
                        + buffer.bytesWritten() + " in buffer to short.", e,
                        true, Long.toString(buffer.bytesWritten()));
            }

            /* remediation parameter */
            // value length = overall message length - header length
            long valueLength = messageLength
                    - PaAttributeTlvFixedLengthEnum.REM_PAR.length();

            if (rpType
                    == PaAttributeRemediationParameterTypeEnum.IETF_URI.id()) {
                PaAttributeValueRemediationParameterUri paramUri =
                        this.uriReader.read(buffer, valueLength);
                builder.setParameter(paramUri);

            } else if (rpType
                  == PaAttributeRemediationParameterTypeEnum.IETF_STRING.id()) {
                PaAttributeValueRemediationParameterString paramString =
                        this.stringReader.read(buffer, valueLength);
                builder.setParameter(paramString);
            } else {
                try {
                    // skip the remaining bytes of the message
                    buffer.read(valueLength);
                } catch (BufferUnderflowException e) {
                    throw new SerializationException("Data length "
                            + buffer.bytesWritten() + " in buffer to short.",
                            e, true, Long.toString(buffer.bytesWritten()));
                }
                // TODO make a default remediation object?
                return null;
            }

            value = (PaAttributeValueRemediationParameters) builder.toObject();

        } catch (RuleException e) {
            throw new ValidationException(e.getMessage(), e, errorOffset);
        }

        return value;
    }

    @Override
    public byte getMinDataLength() {
        return PaAttributeTlvFixedLengthEnum.REM_PAR.length();
    }

}
