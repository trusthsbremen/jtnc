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
package org.ietf.nea.pa.serialize.reader.bytebuffer;

import java.nio.BufferUnderflowException;
import java.nio.charset.Charset;

import org.ietf.nea.pa.attribute.PaAttributeValueInstalledPackages;
import org.ietf.nea.pa.attribute.PaAttributeValueInstalledPackagesBuilder;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;
import org.ietf.nea.pa.attribute.util.PackageEntry;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

/**
 * Reader to parse an integrity measurement installed packages attribute value
 * compliant to RFC 5792 from a buffer of bytes to a Java object.
 *
 * @author Carl-Heinz Genzel
 *
 */
class PaAttributeInstalledPackagesValueReader implements
        ImReader<PaAttributeValueInstalledPackages> {

    private PaAttributeValueInstalledPackagesBuilder baseBuilder;

    /**
     * Creates the reader with a corresponding builder to validate the parsed
     * values and build the attribute value.
     *
     * @param builder the corresponding attribute value builder
     */
    PaAttributeInstalledPackagesValueReader(
            final PaAttributeValueInstalledPackagesBuilder builder) {
        this.baseBuilder = builder;
    }

    @Override
    public PaAttributeValueInstalledPackages read(final ByteBuffer buffer,
            final long messageLength) throws SerializationException,
            ValidationException {

        long errorOffset = 0;

        PaAttributeValueInstalledPackages value = null;
        PaAttributeValueInstalledPackagesBuilder builder =
                (PaAttributeValueInstalledPackagesBuilder) baseBuilder
                .newInstance();

        try {

            try {

                /* ignore reserved */
                errorOffset = buffer.bytesRead();
                buffer.read(2);

                /* pkg count */
                errorOffset = buffer.bytesRead();
                int pkgCount = buffer.readInt((byte) 2);

                long length = messageLength
                        - PaAttributeTlvFixedLengthEnum.INS_PKG.length();

                long counter = 0;
                while (pkgCount > 0 && (length - counter) > 0) {
                    // TODO error offset is vague because it cannot be
                    // calculated to the exact offset.
                    errorOffset = buffer.bytesRead();

                    int nameLength = buffer.readInt((byte) 1);
                    counter++;

                    byte[] pnData = buffer.read(nameLength);
                    String packageName = new String(pnData,
                            Charset.forName("UTF-8"));
                    counter += nameLength;

                    int versionLength = buffer.readInt((byte) 1);
                    counter++;

                    byte[] pvData = buffer.read(versionLength);
                    String packageVersion = new String(pvData,
                            Charset.forName("UTF-8"));
                    counter += versionLength;

                    builder.addPackages(new PackageEntry(packageName,
                            packageVersion));

                    --pkgCount;
                }

            } catch (BufferUnderflowException e) {
                throw new SerializationException("Data length "
                        + buffer.bytesWritten() + " in buffer to short.", e,
                        true, Long.toString(buffer.bytesWritten()));
            }

            value = (PaAttributeValueInstalledPackages) builder.toObject();

        } catch (RuleException e) {
            throw new ValidationException(e.getMessage(), e, errorOffset);
        }

        return value;
    }

    @Override
    public byte getMinDataLength() {

        return PaAttributeTlvFixedLengthEnum.PORT_FT.length();
    }
}
