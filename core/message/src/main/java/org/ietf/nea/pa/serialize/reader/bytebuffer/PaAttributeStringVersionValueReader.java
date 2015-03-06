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
import java.nio.charset.Charset;

import org.ietf.nea.pa.attribute.PaAttributeValueStringVersion;
import org.ietf.nea.pa.attribute.PaAttributeValueStringVersionBuilder;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

/**
 * Reader to parse an integrity measurement string version attribute value
 * compliant to RFC 5792 from a buffer of bytes to a Java object.
 *
 *
 */
class PaAttributeStringVersionValueReader implements
        ImReader<PaAttributeValueStringVersion> {

    private PaAttributeValueStringVersionBuilder baseBuilder;

    /**
     * Creates the reader with a corresponding builder to validate the parsed
     * values and build the attribute value.
     *
     * @param builder the corresponding attribute value builder
     */
    PaAttributeStringVersionValueReader(
            final PaAttributeValueStringVersionBuilder builder) {
        this.baseBuilder = builder;
    }

    @Override
    public PaAttributeValueStringVersion read(final ByteBuffer buffer,
            final long messageLength) throws SerializationException,
            ValidationException {

        long errorOffset = 0;

        PaAttributeValueStringVersion value = null;
        PaAttributeValueStringVersionBuilder builder =
                (PaAttributeValueStringVersionBuilder) this.baseBuilder
                .newInstance();

        try {

            try {

                // First byte is the string length.
                errorOffset = buffer.bytesRead();
                int productLength = buffer.readShort((byte) 1);

                /* product version number */
                errorOffset = buffer.bytesRead();
                byte[] psData = buffer.read(productLength);
                String productVersion = new String(psData,
                        Charset.forName("UTF-8"));
                builder.setProductVersion(productVersion);

                // First byte is the string length.
                errorOffset = buffer.bytesRead();
                int buildLength = buffer.readShort((byte) 1);

                /* build version number */
                errorOffset = buffer.bytesRead();
                byte[] bsData = buffer.read(buildLength);
                String buildNumber = new String(bsData,
                        Charset.forName("UTF-8"));
                builder.setBuildNumber(buildNumber);

                // First byte is the string length.
                errorOffset = buffer.bytesRead();
                int configLength = buffer.readShort((byte) 1);

                /* product version number */
                errorOffset = buffer.bytesRead();
                byte[] csData = buffer.read(configLength);
                String configVersion = new String(csData,
                        Charset.forName("UTF-8"));
                builder.setConfigurationVersion(configVersion);

            } catch (BufferUnderflowException e) {
                throw new SerializationException("Data length "
                        + buffer.bytesWritten() + " in buffer to short.", e,
                        true, Long.toString(buffer.bytesWritten()));
            }

            value = (PaAttributeValueStringVersion) builder.toObject();

        } catch (RuleException e) {
            throw new ValidationException(e.getMessage(), e, errorOffset);
        }

        return value;
    }

    @Override
    public byte getMinDataLength() {

        return PaAttributeTlvFixedLengthEnum.STR_VER.length();
    }
}
