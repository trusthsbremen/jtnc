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
package org.ietf.nea.pb.serialize.writer.bytebuffer;

import java.nio.BufferOverflowException;
import java.nio.charset.Charset;

import org.ietf.nea.pb.message.util.PbMessageValueRemediationParameterString;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.tnccs.serialize.bytebuffer.TnccsWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.util.NotNull;

/**
 * Writer to serialize a TNCCS string remediation parameter compliant to RFC
 * 5793 from a Java object to a buffer of bytes.
 *
 *
 */
class PbMessageRemediationParameterStringValueWriter implements
        TnccsWriter<PbMessageValueRemediationParameterString> {

    @Override
    public void write(final PbMessageValueRemediationParameterString data,
            final ByteBuffer buffer) throws SerializationException {
        NotNull.check("Message value cannot be null.", data);

        PbMessageValueRemediationParameterString mValue = data;

        try {
            /* String length 32 bit(s) */
            buffer.writeUnsignedInt(mValue.getStringLength());

            /* reason String */
            buffer.write(mValue.getRemediationString().getBytes(
                    Charset.forName("UTF-8")));

            /* language code length 8 bit(s) */
            buffer.writeUnsignedByte(mValue.getLangCodeLength());

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
