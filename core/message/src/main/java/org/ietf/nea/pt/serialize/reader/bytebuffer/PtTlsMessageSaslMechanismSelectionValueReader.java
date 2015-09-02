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
package org.ietf.nea.pt.serialize.reader.bytebuffer;

import java.nio.BufferUnderflowException;
import java.nio.charset.Charset;

import org.ietf.nea.pt.message.enums.PtTlsMessageTlvFixedLengthEnum;
import org.ietf.nea.pt.value.PtTlsMessageValueSaslMechanismSelection;
import org.ietf.nea.pt.value.PtTlsMessageValueSaslMechanismSelectionBuilder;
import org.ietf.nea.pt.value.util.SaslMechanismEntry;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.t.serialize.bytebuffer.TransportReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

/**
 * Reader to parse a transport SASL mechanism selection message value compliant
 * to RFC 6876 from a buffer of bytes to a Java object.
 *
 *
 */
class PtTlsMessageSaslMechanismSelectionValueReader implements
        TransportReader<PtTlsMessageValueSaslMechanismSelection> {

    private PtTlsMessageValueSaslMechanismSelectionBuilder baseBuilder;

    /**
     * Creates the reader with a corresponding builder to validate the parsed
     * values and build the message value.
     *
     * @param builder the corresponding message value builder
     */
    PtTlsMessageSaslMechanismSelectionValueReader(
            final PtTlsMessageValueSaslMechanismSelectionBuilder builder) {
        this.baseBuilder = builder;
    }

    @Override
    public PtTlsMessageValueSaslMechanismSelection read(
            final ByteBuffer buffer, final long length)
            throws SerializationException, ValidationException {

        // ignore any given length and find out on your own.

        long errorOffset = 0;

        PtTlsMessageValueSaslMechanismSelection mValue = null;
        PtTlsMessageValueSaslMechanismSelectionBuilder builder =
                (PtTlsMessageValueSaslMechanismSelectionBuilder)
                this.baseBuilder.newInstance();

        try {
            try {

                /* reserved + name length */
                errorOffset = buffer.bytesRead();
                byte b = buffer.readByte();
                int nameLength = b & 0x1F;

                /* name */
                byte[] sData = buffer.read(nameLength);
                String name = new String(sData, Charset.forName("US-ASCII"));
                SaslMechanismEntry mech = new SaslMechanismEntry(name);
                builder.setMechanism(mech);

                /* optional initial SASL data */
                long counter = (length - 1) - sData.length;
                if (counter > 0) {
                    errorOffset = buffer.bytesRead();
                    /*
                     * FIXME: the length may be shortened here, to respect the
                     * maximum Java string length.
                     */
                    int safeLength = (counter <= Integer.MAX_VALUE)
                            ? (int) counter
                            : Integer.MAX_VALUE;

                    byte[] initialSaslMsg = buffer.read((int) safeLength);
                    if (counter > safeLength) {
                        // skip the rest, that does not fit
                        buffer.read(counter - safeLength);
                    }
                    builder.setOptionalInitialSaslMessage(initialSaslMsg);
                }

            } catch (BufferUnderflowException e) {
                throw new SerializationException("Data length "
                        + buffer.bytesWritten() + " in buffer to short.", e,
                        true, Long.toString(buffer.bytesWritten()));
            }

            mValue = (PtTlsMessageValueSaslMechanismSelection) builder
                    .toObject();

        } catch (RuleException e) {
            throw new ValidationException(e.getMessage(), e, errorOffset);
        }

        return mValue;
    }

    @Override
    public byte getMinDataLength() {
        return PtTlsMessageTlvFixedLengthEnum.SASL_SEL.length();
    }
}
