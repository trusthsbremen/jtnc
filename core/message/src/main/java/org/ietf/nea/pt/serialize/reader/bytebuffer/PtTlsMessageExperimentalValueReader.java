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

import org.ietf.nea.pt.value.PtTlsMessageValueExperimental;
import org.ietf.nea.pt.value.PtTlsMessageValueExperimentalBuilder;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.t.serialize.bytebuffer.TransportReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

/**
 * Reader to parse a transport experimental message value compliant to RFC 6876
 * from a buffer of bytes to a Java object.
 *
 * @author Carl-Heinz Genzel
 *
 */
class PtTlsMessageExperimentalValueReader implements
        TransportReader<PtTlsMessageValueExperimental> {

    private PtTlsMessageValueExperimentalBuilder baseBuilder;

    /**
     * Creates the reader with a corresponding builder to validate the parsed
     * values and build the message value.
     *
     * @param builder the corresponding message value builder
     */
    PtTlsMessageExperimentalValueReader(
            final PtTlsMessageValueExperimentalBuilder builder) {
        this.baseBuilder = builder;
    }

    @Override
    public PtTlsMessageValueExperimental read(final ByteBuffer buffer,
            final long length) throws SerializationException,
            ValidationException {

        // ignore any given length and find out on your own.

        long errorOffset = 0;

        PtTlsMessageValueExperimental mValue = null;
        PtTlsMessageValueExperimentalBuilder builder =
                (PtTlsMessageValueExperimentalBuilder) baseBuilder
                .newInstance();

        try {
            try {

                /* message */
                errorOffset = buffer.bytesRead();
                /*
                 * FIXME: the length may be shortened here, to respect the
                 * maximum Java string length.
                 */
                int safeLength = (length <= Integer.MAX_VALUE) ? (int) length
                        : Integer.MAX_VALUE;

                byte[] sData = buffer.read((int) safeLength);
                if (length > safeLength) {
                    // skip the rest that does not fit
                    buffer.read(length - safeLength);
                }
                String message = new String(sData, Charset.forName("UTF-8"));
                builder.setMessage(message);

            } catch (BufferUnderflowException e) {
                throw new SerializationException("Data length "
                        + buffer.bytesWritten() + " in buffer to short.", e,
                        true, Long.toString(buffer.bytesWritten()));
            }

            mValue = (PtTlsMessageValueExperimental) builder.toObject();

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
