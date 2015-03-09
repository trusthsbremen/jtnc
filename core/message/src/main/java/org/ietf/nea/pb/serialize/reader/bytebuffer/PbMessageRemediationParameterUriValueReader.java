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
package org.ietf.nea.pb.serialize.reader.bytebuffer;

import java.nio.BufferUnderflowException;
import java.nio.charset.Charset;

import org.ietf.nea.pb.message.util.PbMessageValueRemediationParameterUri;
import org.ietf.nea.pb.message.util
.PbMessageValueRemediationParameterUriBuilder;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.serialize.bytebuffer.TnccsReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.util.NotNull;

/**
 * Reader to parse a TNCCS URI remediation parameter compliant to RFC 5793 from
 * a buffer of bytes to a Java object.
 *
 *
 */
class PbMessageRemediationParameterUriValueReader implements
        TnccsReader<PbMessageValueRemediationParameterUri> {

    private PbMessageValueRemediationParameterUriBuilder baseBuilder;

    /**
     * Creates the reader with a corresponding builder to validate the parsed
     * values and build the parameter.
     *
     * @param builder the corresponding parameter builder
     */
    PbMessageRemediationParameterUriValueReader(
            final PbMessageValueRemediationParameterUriBuilder builder) {
        this.baseBuilder = builder;
    }

    @Override
    public PbMessageValueRemediationParameterUri read(final ByteBuffer buffer,
            final long messageLength) throws SerializationException,
            ValidationException {

        NotNull.check("Buffer cannot be null.", buffer);

        long errorOffset = 0;

        PbMessageValueRemediationParameterUri value = null;
        PbMessageValueRemediationParameterUriBuilder builder =
                (PbMessageValueRemediationParameterUriBuilder) this.baseBuilder
                .newInstance();

        try {

            try {

                /* URI */
                errorOffset = buffer.bytesRead();
                /* FIXME: the length may be shortened here, to respect the
                 * maximum Java string length.
                 */
                int safeLength = (messageLength <= Integer.MAX_VALUE)
                ? (int) messageLength : Integer.MAX_VALUE;

                byte[] sData = buffer.read((int) safeLength);
                if (messageLength > safeLength) {
                    // skip the rest, that does not fit
                    buffer.read(messageLength - safeLength);
                }
                String uriString = new String(sData,
                        Charset.forName("US-ASCII"));
                builder.setUri(uriString);

            } catch (BufferUnderflowException e) {
                throw new SerializationException("Data length "
                        + buffer.bytesWritten() + " in buffer to short.", e,
                        true, Long.toString(buffer.bytesWritten()));
            }

            value = (PbMessageValueRemediationParameterUri) builder.toObject();

        } catch (RuleException e) {
            throw new ValidationException(e.getMessage(), e, errorOffset);
        }

        return value;
    }

    @Override
    public byte getMinDataLength() {

        // no minimal length
        return 0;
    }
}
