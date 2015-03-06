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
package org.ietf.nea.pa.serialize.reader.bytebuffer;

import java.nio.BufferUnderflowException;

import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;
import org.ietf.nea.pa.attribute.util
.PaAttributeValueErrorInformationUnsupportedVersion;
import org.ietf.nea.pa.attribute.util
.PaAttributeValueErrorInformationUnsupportedVersionBuilder;
import org.ietf.nea.pa.attribute.util.MessageHeaderDump;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

/**
 * Reader to parse an integrity measurement unsupported version error
 * information compliant to RFC 5792 from a buffer of bytes to a Java object.
 *
 *
 */
class PaAttributeErrorInformationUnsupportedVersionValueReader implements
        ImReader<PaAttributeValueErrorInformationUnsupportedVersion> {

    private PaAttributeValueErrorInformationUnsupportedVersionBuilder
        baseBuilder;

    /**
     * Creates the reader with a corresponding builder to validate the parsed
     * values and build the information.
     *
     * @param builder the corresponding information builder
     */
    PaAttributeErrorInformationUnsupportedVersionValueReader(
            final PaAttributeValueErrorInformationUnsupportedVersionBuilder
                builder) {
        this.baseBuilder = builder;
    }

    @Override
    public PaAttributeValueErrorInformationUnsupportedVersion read(
            final ByteBuffer buffer, final long messageLength)
            throws SerializationException, ValidationException {

        long errorOffset = 0;

        PaAttributeValueErrorInformationUnsupportedVersion value = null;
        PaAttributeValueErrorInformationUnsupportedVersionBuilder builder =
                (PaAttributeValueErrorInformationUnsupportedVersionBuilder)
                this.baseBuilder.newInstance();

        try {

            try {

                /* message header */
                /* copy version */
                errorOffset = buffer.bytesRead();
                short version = buffer.readShort((byte) 1);

                /* copy reserved */
                byte[] reserved = buffer.read(3);

                /* copy identifier */
                long identifier = buffer.readLong((byte) 4);

                builder.setMessageHeader(new MessageHeaderDump(version,
                        reserved, identifier));

                /* max version */
                errorOffset = buffer.bytesRead();
                short maxVersion = buffer.readShort((byte) 1);
                builder.setMaxVersion(maxVersion);

                /* min version */
                errorOffset = buffer.bytesRead();
                short minVersion = buffer.readShort((byte) 1);
                builder.setMinVersion(minVersion);

                /* ignore reserved */
                errorOffset = buffer.bytesRead();
                buffer.read(2);

            } catch (BufferUnderflowException e) {
                throw new SerializationException("Data length "
                        + buffer.bytesWritten() + " in buffer to short.", e,
                        true, Long.toString(buffer.bytesWritten()));
            }

            value = (PaAttributeValueErrorInformationUnsupportedVersion) builder
                    .toObject();

        } catch (RuleException e) {
            throw new ValidationException(e.getMessage(), e, errorOffset);
        }

        return value;
    }

    @Override
    public byte getMinDataLength() {

        final int fixedMinMaxLength = 4;
        return (byte) (PaAttributeTlvFixedLengthEnum.ERR_INF.length()
                + fixedMinMaxLength);
    }
}
