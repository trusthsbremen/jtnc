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
package org.ietf.nea.pt.serialize.reader.bytebuffer;

import java.nio.BufferUnderflowException;

import org.ietf.nea.pt.message.PtTlsMessageHeader;
import org.ietf.nea.pt.message.PtTlsMessageHeaderBuilder;
import org.ietf.nea.pt.message.enums.PtTlsMessageTlvFixedLengthEnum;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.t.serialize.bytebuffer.TransportReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

/**
 * Reader to parse a transport message header compliant to RFC 6876
 * from a buffer of bytes to a Java object.
 *
 *
 */
class PtTlsMessageHeaderReader implements TransportReader<PtTlsMessageHeader> {

    private PtTlsMessageHeaderBuilder baseBuilder;

    /**
     * Creates the reader with a corresponding builder to validate the parsed
     * values and build the message value.
     *
     * @param builder the corresponding message value builder
     */
    PtTlsMessageHeaderReader(final PtTlsMessageHeaderBuilder builder) {
        this.baseBuilder = builder;
    }

    @Override
    public PtTlsMessageHeader read(final ByteBuffer buffer, final long length)
            throws SerializationException, ValidationException {

        // ignore any given length and find out on your own.

        long errorOffset = 0;

        PtTlsMessageHeader mHeader = null;
        PtTlsMessageHeaderBuilder builder =
                (PtTlsMessageHeaderBuilder) this.baseBuilder.newInstance();

        try {
            try {

                /* reserved */
                errorOffset = buffer.bytesRead();
                buffer.readByte();

                /* vendor ID */
                errorOffset = buffer.bytesRead();
                long vendorId = buffer.readLong((byte) 3);
                builder.setVendorId(vendorId);

                /* message type */
                errorOffset = buffer.bytesRead();
                long messageType = buffer.readLong((byte) 4);
                builder.setType(messageType);

                /* message length */
                errorOffset = buffer.bytesRead();
                long mLength = buffer.readLong((byte) 4);
                builder.setLength(mLength);

                /* message identifier */
                errorOffset = buffer.bytesRead();
                long identifier = buffer.readLong((byte) 4);
                builder.setIdentifier(identifier);

            } catch (BufferUnderflowException e) {
                throw new SerializationException("Data length "
                        + buffer.bytesWritten() + " in buffer to short.", e,
                        true, Long.toString(buffer.bytesWritten()));
            }

            mHeader = (PtTlsMessageHeader) builder.toObject();

        } catch (RuleException e) {
            throw new ValidationException(e.getMessage(), e, errorOffset);
        }

        return mHeader;
    }

    @Override
    public byte getMinDataLength() {
        return PtTlsMessageTlvFixedLengthEnum.MESSAGE.length();
    }

}
