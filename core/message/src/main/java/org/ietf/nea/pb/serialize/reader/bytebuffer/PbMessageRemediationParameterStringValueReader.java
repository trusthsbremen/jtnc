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

import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLengthEnum;
import org.ietf.nea.pb.message.util.PbMessageValueRemediationParameterString;
import org.ietf.nea.pb.message.util
.PbMessageValueRemediationParameterStringBuilder;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.serialize.bytebuffer.TnccsReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.util.NotNull;

/**
 * Reader to parse a TNCCS string remediation parameter compliant to RFC 5793
 * from a buffer of bytes to a Java object.
 *
 *
 */
class PbMessageRemediationParameterStringValueReader implements
        TnccsReader<PbMessageValueRemediationParameterString> {

    private PbMessageValueRemediationParameterStringBuilder baseBuilder;

    /**
     * Creates the reader with a corresponding builder to validate the parsed
     * values and build the parameter.
     *
     * @param builder the corresponding parameter builder
     */
    PbMessageRemediationParameterStringValueReader(
            final PbMessageValueRemediationParameterStringBuilder builder) {
        this.baseBuilder = builder;
    }

    @Override
    public PbMessageValueRemediationParameterString read(
            final ByteBuffer buffer, final long messageLength)
            throws SerializationException, ValidationException {

        NotNull.check("Buffer cannot be null.", buffer);

        long errorOffset = 0;

        PbMessageValueRemediationParameterString value = null;
        PbMessageValueRemediationParameterStringBuilder builder =
                (PbMessageValueRemediationParameterStringBuilder) baseBuilder
                .newInstance();

        try {

            try {

                /* first 4 bytes are the remediation string length */
                errorOffset = buffer.bytesRead();
                long stringLength = buffer.readLong((byte) 4);

                /* remediation string */
                errorOffset = buffer.bytesRead();
                /* FIXME: the length may be shortened here, to respect the
                 * maximum Java string length.
                 */
                int safeLength = (stringLength <= Integer.MAX_VALUE)
                ? (int) stringLength : Integer.MAX_VALUE;

                byte[] sData = buffer.read((int) safeLength);
                if (stringLength > safeLength) {
                    // skip the rest that does not fit
                    buffer.read(stringLength - safeLength);
                }
                String remString = new String(sData,
                        Charset.forName("UTF-8"));
                builder.setRemediationString(remString);

                /* next byte is the language code length */
                errorOffset = buffer.bytesRead();
                short langLength = buffer.readShort((byte) 1);

                /* language code */
                errorOffset = buffer.bytesRead();
                byte[] lsData = buffer.read(langLength);
                String langCode = new String(lsData,
                        Charset.forName("US-ASCII"));
                builder.setLangCode(langCode);

            } catch (BufferUnderflowException e) {
                throw new SerializationException("Data length "
                        + buffer.bytesWritten() + " in buffer to short.", e,
                        true, Long.toString(buffer.bytesWritten()));
            }

            value = (PbMessageValueRemediationParameterString) builder
                    .toObject();

        } catch (RuleException e) {
            throw new ValidationException(e.getMessage(), e, errorOffset);
        }

        return value;
    }

    @Override
    public byte getMinDataLength() {

        return PbMessageTlvFixedLengthEnum.REA_STR_VALUE.length();
    }
}
