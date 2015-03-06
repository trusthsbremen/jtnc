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
 * @author Carl-Heinz Genzel
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
