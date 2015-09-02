/**
 * The BSD 3-Clause License ("BSD New" or "BSD Simplified")
 *
 * Copyright © 2015 Trust HS Bremen and its Contributors. All rights   
 * reserved.
 *
 * See the CONTRIBUTORS file distributed with this work for additional
 * information regarding copyright ownership.
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
package org.ietf.nea.pa.serialize.writer.bytebuffer;

import java.nio.BufferOverflowException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.ietf.nea.pa.attribute.PaAttributeValueOperationalStatus;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.util.NotNull;

/**
 * Writer to serialize an integrity measurement operational status attribute
 * value compliant to RFC 5792 from a Java object to a buffer of bytes.
 *
 *
 */
class PaAttributeOperationalStatusValueWriter implements
        ImWriter<PaAttributeValueOperationalStatus> {

    private static final byte[] RESERVED = new byte[] {0, 0};

    private SimpleDateFormat dateFormater;

    /**
     * Creates the writer and initializes a date formatter according
     * to the specifications of the RFC 5792 for the last use date.
     *
     */
    PaAttributeOperationalStatusValueWriter() {
        this.dateFormater = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        this.dateFormater.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    @Override
    public void write(final PaAttributeValueOperationalStatus data,
            final ByteBuffer buffer) throws SerializationException {
        NotNull.check("Value cannot be null.", data);

        NotNull.check("Buffer cannot be null.", buffer);

        PaAttributeValueOperationalStatus aValue = data;

        try {
            /* status 8 bit(s) */
            buffer.writeUnsignedByte(aValue.getStatus().id());

            /* result 8 bit(s) */
            buffer.writeUnsignedByte(aValue.getResult().id());

            /* reserved 24 bit(s) */
            buffer.write(RESERVED);

            /* last use 160 bit(s) */
            buffer.write(this.dateFormater.format(aValue.getLastUse())
                    .getBytes(Charset.forName("US-ASCII")));

        } catch (BufferOverflowException e) {
            throw new SerializationException("Buffer capacity "
                    + buffer.capacity() + " to short.", e, false,
                    Long.toString(buffer.capacity()));
        }
    }

}
