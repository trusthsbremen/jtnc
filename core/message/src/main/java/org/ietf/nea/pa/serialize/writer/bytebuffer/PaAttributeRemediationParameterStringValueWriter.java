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
package org.ietf.nea.pa.serialize.writer.bytebuffer;

import java.nio.BufferOverflowException;
import java.nio.charset.Charset;

import org.ietf.nea.pa.attribute.util
.PaAttributeValueRemediationParameterString;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.util.NotNull;

/**
 * Writer to serialize an integrity measurement string remediation parameter
 * compliant to RFC 5792 from a Java object to a buffer of bytes.
 *
 * @author Carl-Heinz Genzel
 *
 */
class PaAttributeRemediationParameterStringValueWriter implements
        ImWriter<PaAttributeValueRemediationParameterString> {

    @Override
    public void write(final PaAttributeValueRemediationParameterString data,
            final ByteBuffer buffer) throws SerializationException {
        NotNull.check("Value cannot be null.", data);

        NotNull.check("Buffer cannot be null.", buffer);

        PaAttributeValueRemediationParameterString mValue = data;

        try {

            /* String length 32 bit(s) */
            buffer.writeUnsignedInt(mValue.getStringLength());

            /* reason String */
            buffer.write(mValue.getRemediationString().getBytes(
                    Charset.forName("UTF-8")));

            /* language code length 8 bit(s) */
            buffer.writeUnsignedShort(mValue.getLangCodeLength());

            /* language code */
            buffer.write(mValue.getLangCode().getBytes(
                    Charset.forName("US-ASCII")));

        } catch (BufferOverflowException e) {
            throw new SerializationException("Buffer capacity "
                    + buffer.capacity() + " to short.", e, false,
                    Long.toString(buffer.capacity()));
        }
    }

}
