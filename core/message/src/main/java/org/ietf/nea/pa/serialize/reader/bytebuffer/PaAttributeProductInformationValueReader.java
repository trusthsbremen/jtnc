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

import org.ietf.nea.pa.attribute.PaAttributeValueProductInformation;
import org.ietf.nea.pa.attribute.PaAttributeValueProductInformationBuilder;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

/**
 * Reader to parse an integrity measurement product information attribute value
 * compliant to RFC 5792 from a buffer of bytes to a Java object.
 *
 *
 */
class PaAttributeProductInformationValueReader implements
        ImReader<PaAttributeValueProductInformation> {

    private PaAttributeValueProductInformationBuilder baseBuilder;

    /**
     * Creates the reader with a corresponding builder to validate the parsed
     * values and build the attribute value.
     *
     * @param builder the corresponding attribute value builder
     */
    PaAttributeProductInformationValueReader(
            final PaAttributeValueProductInformationBuilder builder) {
        this.baseBuilder = builder;
    }

    @Override
    public PaAttributeValueProductInformation read(final ByteBuffer buffer,
            final long messageLength) throws SerializationException,
            ValidationException {

        long errorOffset = 0;

        PaAttributeValueProductInformation value = null;
        PaAttributeValueProductInformationBuilder builder =
                (PaAttributeValueProductInformationBuilder) this.baseBuilder
                .newInstance();

        try {

            try {

                /* vendor ID */
                errorOffset = buffer.bytesRead();
                long vendorId = buffer.readLong((byte) 3);
                builder.setVendorId(vendorId);

                /* product id */
                errorOffset = buffer.bytesRead();
                int productId = buffer.readInt((byte) 2);
                builder.setProductId(productId);

                /* product name */
                errorOffset = buffer.bytesRead();
                long nameLength = messageLength
                        - PaAttributeTlvFixedLengthEnum.PRO_INF.length();
                /* FIXME: the length may be shortened here, to respect the
                 * maximum Java string length.
                 */
                int safeLength = (nameLength <= Integer.MAX_VALUE)
                ? (int) nameLength : Integer.MAX_VALUE;

                byte[] sData = buffer.read((int) safeLength);
                if (nameLength > safeLength) {
                    // skip the rest that does not fit
                    buffer.read(nameLength - safeLength);
                }
                String productName = new String(sData,
                        Charset.forName("UTF-8"));
                builder.setName(productName);

            } catch (BufferUnderflowException e) {
                throw new SerializationException("Data length "
                        + buffer.bytesWritten() + " in buffer to short.", e,
                        true, Long.toString(buffer.bytesWritten()));
            }

            value = (PaAttributeValueProductInformation) builder.toObject();

        } catch (RuleException e) {
            throw new ValidationException(e.getMessage(), e, errorOffset);
        }

        return value;
    }

    @Override
    public byte getMinDataLength() {
        return PaAttributeTlvFixedLengthEnum.PRO_INF.length();
    }

}
