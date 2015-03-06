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
 * @author Carl-Heinz Genzel
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
                        // skip the rest that does not fit
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
