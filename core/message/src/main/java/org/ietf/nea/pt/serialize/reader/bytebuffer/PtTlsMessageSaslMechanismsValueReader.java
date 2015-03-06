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

import org.ietf.nea.pt.value.PtTlsMessageValueSaslMechanisms;
import org.ietf.nea.pt.value.PtTlsMessageValueSaslMechanismsBuilder;
import org.ietf.nea.pt.value.util.SaslMechanismEntry;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.t.serialize.bytebuffer.TransportReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

/**
 * Reader to parse a transport SASL mechanisms message value compliant to RFC
 * 6876 from a buffer of bytes to a Java object.
 *
 * @author Carl-Heinz Genzel
 *
 */
class PtTlsMessageSaslMechanismsValueReader implements
        TransportReader<PtTlsMessageValueSaslMechanisms> {

    private PtTlsMessageValueSaslMechanismsBuilder baseBuilder;

    /**
     * Creates the reader with a corresponding builder to validate the parsed
     * values and build the message value.
     *
     * @param builder the corresponding message value builder
     */
    PtTlsMessageSaslMechanismsValueReader(
            final PtTlsMessageValueSaslMechanismsBuilder builder) {
        this.baseBuilder = builder;
    }

    @Override
    public PtTlsMessageValueSaslMechanisms read(final ByteBuffer buffer,
            final long length) throws SerializationException,
            ValidationException {

        // ignore any given length and find out on your own.

        long errorOffset = 0;

        PtTlsMessageValueSaslMechanisms mValue = null;
        PtTlsMessageValueSaslMechanismsBuilder builder =
                (PtTlsMessageValueSaslMechanismsBuilder) this.baseBuilder
                .newInstance();

        try {
            try {

                long counter = 0;
                while ((length - counter) > 0) {
                    // TODO error offset is vague because it cannot be
                    // calculated to the exact offset.
                    errorOffset = buffer.bytesRead();

                    /* reserved + name length */
                    byte b = buffer.readByte();
                    int nameLength = b & 0x1F;
                    counter++;

                    /* name */
                    byte[] sData = buffer.read(nameLength);
                    String name = new String(sData,
                            Charset.forName("US-ASCII"));
                    SaslMechanismEntry mech = new SaslMechanismEntry(name);
                    counter += sData.length;

                    builder.addMechanism(mech);
                }

            } catch (BufferUnderflowException e) {
                throw new SerializationException("Data length "
                        + buffer.bytesWritten() + " in buffer to short.", e,
                        true, Long.toString(buffer.bytesWritten()));
            }

            mValue = (PtTlsMessageValueSaslMechanisms) builder.toObject();

        } catch (RuleException e) {
            throw new ValidationException(e.getMessage(), e, errorOffset);
        }

        return mValue;
    }

    @Override
    public byte getMinDataLength() {
        return 0;
    }
}
