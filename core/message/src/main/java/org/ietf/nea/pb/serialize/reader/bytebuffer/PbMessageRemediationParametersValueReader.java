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
package org.ietf.nea.pb.serialize.reader.bytebuffer;

import java.nio.BufferUnderflowException;

import org.ietf.nea.pb.message.PbMessageValueRemediationParameters;
import org.ietf.nea.pb.message.PbMessageValueRemediationParametersBuilder;
import org.ietf.nea.pb.message.enums.PbMessageRemediationParameterTypeEnum;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLengthEnum;
import org.ietf.nea.pb.message.util.PbMessageValueRemediationParameterString;
import org.ietf.nea.pb.message.util.PbMessageValueRemediationParameterUri;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.serialize.bytebuffer.TnccsReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.util.NotNull;

/**
 * Reader to parse a TNCCS remediation parameters message value compliant to RFC
 * 5793 from a buffer of bytes to a Java object.
 *
 *
 */
class PbMessageRemediationParametersValueReader implements
        TnccsReader<PbMessageValueRemediationParameters> {

    private PbMessageValueRemediationParametersBuilder baseBuilder;

    // TODO should be a map to make the remediation parameters more customizable
    private final PbMessageRemediationParameterStringValueReader stringReader;
    private final PbMessageRemediationParameterUriValueReader uriReader;

    /**
     * Creates the reader with a corresponding builder to validate the parsed
     * values and build the message value. The given readers are responsible
     * to parse the contained supporting remediation parameter.
     *
     * @param builder the corresponding message value builder
     * @param stringReader the reader to parse a string remediation parameter
     * @param uriReader the reader to parse a URI remediation parameter
     */
    public PbMessageRemediationParametersValueReader(
            final PbMessageValueRemediationParametersBuilder builder,
            final PbMessageRemediationParameterStringValueReader stringReader,
            final PbMessageRemediationParameterUriValueReader uriReader) {
        this.baseBuilder = builder;
        this.stringReader = stringReader;
        this.uriReader = uriReader;
    }

    @Override
    public PbMessageValueRemediationParameters read(final ByteBuffer buffer,
            final long messageLength) throws SerializationException,
            ValidationException {

        NotNull.check("Buffer cannot be null.", buffer);

        long errorOffset = 0;

        PbMessageValueRemediationParameters value = null;
        PbMessageValueRemediationParametersBuilder builder =
                (PbMessageValueRemediationParametersBuilder) this.baseBuilder
                .newInstance();

        long rpVendorId = 0L;
        long rpType = 0L;

        try {

            try {

                /* ignore reserved 8 bit(s) */
                errorOffset = buffer.bytesRead();
                buffer.readByte();

                /* vendor ID 24 bit(s) */
                errorOffset = buffer.bytesRead();
                rpVendorId = buffer.readLong((byte) 3);
                builder.setRpVendorId(rpVendorId);

                /* remediation type 32 bit(s) */
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
            long valueLength = messageLength - this.getMinDataLength();

            if (rpType == PbMessageRemediationParameterTypeEnum.IETF_URI.id()) {
                PbMessageValueRemediationParameterUri paramUri =
                        this.uriReader.read(buffer, valueLength);
                builder.setParameter(paramUri);

            } else if (rpType
                    == PbMessageRemediationParameterTypeEnum.IETF_STRING.id()) {
                PbMessageValueRemediationParameterString paramString =
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

            value = (PbMessageValueRemediationParameters) builder.toObject();

        } catch (RuleException e) {
            throw new ValidationException(e.getMessage(), e, errorOffset);
        }

        return value;
    }

    @Override
    public byte getMinDataLength() {
        return PbMessageTlvFixedLengthEnum.REM_PAR_VALUE.length();
    }

}
