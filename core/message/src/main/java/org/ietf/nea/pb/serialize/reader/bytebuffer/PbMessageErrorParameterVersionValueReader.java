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
package org.ietf.nea.pb.serialize.reader.bytebuffer;

import java.nio.BufferUnderflowException;

import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLengthEnum;
import org.ietf.nea.pb.message.util.PbMessageValueErrorParameterVersion;
import org.ietf.nea.pb.message.util.PbMessageValueErrorParameterVersionBuilder;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.serialize.bytebuffer.TnccsReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.util.NotNull;

/**
 * Reader to parse a TNCCS unsupported version error
 * parameter compliant to RFC 5793 from a buffer of bytes to a Java object.
 *
 *
 */
class PbMessageErrorParameterVersionValueReader implements
        TnccsReader<PbMessageValueErrorParameterVersion> {

    private PbMessageValueErrorParameterVersionBuilder baseBuilder;

    /**
     * Creates the reader with a corresponding builder to validate the parsed
     * values and build the parameter.
     *
     * @param builder the corresponding parameter builder
     */
    PbMessageErrorParameterVersionValueReader(
            final PbMessageValueErrorParameterVersionBuilder builder) {
        this.baseBuilder = builder;
    }

    @Override
    public PbMessageValueErrorParameterVersion read(final ByteBuffer buffer,
            final long messageLength) throws SerializationException,
            ValidationException {

        NotNull.check("Buffer cannot be null.", buffer);

        long errorOffset = 0;

        PbMessageValueErrorParameterVersion value = null;
        PbMessageValueErrorParameterVersionBuilder builder =
                (PbMessageValueErrorParameterVersionBuilder) this.baseBuilder
                .newInstance();

        try {

            try {

                /* bad version 8 bit(s) */
                errorOffset = buffer.bytesRead();
                short badVersion = buffer.readShort((byte) 1);
                builder.setBadVersion(badVersion);

                /* max version 8 bit(s) */
                errorOffset = buffer.bytesRead();
                short maxVersion = buffer.readShort((byte) 1);
                builder.setMaxVersion(maxVersion);

                /* min version 8 bit(s) */
                errorOffset = buffer.bytesRead();
                short minVersion = buffer.readShort((byte) 1);
                builder.setMinVersion(minVersion);

                /* ignore reserved 8 bit(s) */
                errorOffset = buffer.bytesRead();
                buffer.readByte();

            } catch (BufferUnderflowException e) {
                throw new SerializationException("Data length "
                        + buffer.bytesWritten() + " in buffer to short.", e,
                        true, Long.toString(buffer.bytesWritten()));
            }

            value = (PbMessageValueErrorParameterVersion) builder.toObject();

        } catch (RuleException e) {
            throw new ValidationException(e.getMessage(), e, errorOffset);
        }

        return value;
    }

    @Override
    public byte getMinDataLength() {

        return PbMessageTlvFixedLengthEnum.ERR_SUB_VALUE.length();
    }
}
