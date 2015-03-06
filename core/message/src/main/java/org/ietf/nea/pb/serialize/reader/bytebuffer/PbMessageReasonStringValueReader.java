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
import java.nio.charset.Charset;

import org.ietf.nea.pb.message.PbMessageValueReasonString;
import org.ietf.nea.pb.message.PbMessageValueReasonStringBuilder;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLengthEnum;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.serialize.bytebuffer.TnccsReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.util.NotNull;

/**
 * Reader to parse a TNCCS reason string message value compliant to RFC 5793
 * from a buffer of bytes to a Java object.
 *
 *
 */
class PbMessageReasonStringValueReader implements
        TnccsReader<PbMessageValueReasonString> {

    private PbMessageValueReasonStringBuilder baseBuilder;

    /**
     * Creates the reader with a corresponding builder to validate the parsed
     * values and build the message value.
     *
     * @param builder the corresponding message value builder
     */
    PbMessageReasonStringValueReader(
            final PbMessageValueReasonStringBuilder builder) {
        this.baseBuilder = builder;
    }

    @Override
    public PbMessageValueReasonString read(final ByteBuffer buffer,
            final long messageLength) throws SerializationException,
            ValidationException {

        NotNull.check("Buffer cannot be null.", buffer);

        long errorOffset = 0;

        PbMessageValueReasonString value = null;
        PbMessageValueReasonStringBuilder builder =
                (PbMessageValueReasonStringBuilder) this.baseBuilder
                .newInstance();

        try {

            try {

                /* First 4 bytes are the reason string length. */
                errorOffset = buffer.bytesRead();
                long reasonLength = buffer.readLong((byte) 4);

                /* reason string */
                errorOffset = buffer.bytesRead();
                /* FIXME: the length may be shortened here, to respect the
                 * maximum Java string length.
                 */
                int safeLength = (reasonLength <= Integer.MAX_VALUE)
                ? (int) reasonLength : Integer.MAX_VALUE;

                byte[] sData = buffer.read((int) safeLength);
                if (reasonLength > safeLength) {
                    // skip the rest that does not fit
                    buffer.read(reasonLength - safeLength);
                }
                String reasonString = new String(sData,
                        Charset.forName("UTF-8"));
                builder.setReasonString(reasonString);

                /* Last byte is the language code length */
                errorOffset = buffer.bytesRead();
                short langLength = buffer.readShort((byte) 1);

                /* language code */
                errorOffset = buffer.bytesRead();
                byte[] lData = buffer.read(langLength);
                String langCode = new String(lData,
                        Charset.forName("US-ASCII"));
                builder.setLangCode(langCode);

            } catch (BufferUnderflowException e) {
                throw new SerializationException("Data length "
                        + buffer.bytesWritten() + " in buffer to short.", e,
                        true, Long.toString(buffer.bytesWritten()));
            }

            value = (PbMessageValueReasonString) builder.toObject();

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
