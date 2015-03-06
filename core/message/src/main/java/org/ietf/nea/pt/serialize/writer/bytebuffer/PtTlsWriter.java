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
package org.ietf.nea.pt.serialize.writer.bytebuffer;

import java.util.HashMap;
import java.util.Map;

import org.ietf.nea.pt.message.PtTlsMessage;
import org.ietf.nea.pt.message.PtTlsMessageHeader;
import org.ietf.nea.pt.message.enums.PtTlsMessageTlvFixedLengthEnum;
import org.ietf.nea.pt.value.PtTlsMessageValue;

import de.hsbremen.tc.tnc.message.Combined;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.t.message.TransportMessage;
import de.hsbremen.tc.tnc.message.t.serialize.bytebuffer.TransportWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.util.NotNull;

/**
 * Writer to serialize an entire transport message compliant to RFC 6876 from a
 * Java object to a buffer of bytes. The writer is composed of several writers
 * that are used to serialize the different message elements (e.g. header,
 * message , ...). Message value writers can be managed with the add and remove
 * operations.
 *
 *
 */
public class PtTlsWriter implements TransportWriter<TransportMessage>,
        Combined<TransportWriter<PtTlsMessageValue>> {

    private final TransportWriter<PtTlsMessageHeader> mHeadWriter;
    private final Map<Long, Map<Long, TransportWriter<PtTlsMessageValue>>>
        valueWriters;

    /**
     * Creates the writer with a message header writer to serialize RFC 6876
     * compliant headers.
     *
     * @param mHeadWriter the message header writer
     */
    PtTlsWriter(final TransportWriter<PtTlsMessageHeader> mHeadWriter) {
        this(mHeadWriter,
           new HashMap<Long, Map<Long, TransportWriter<PtTlsMessageValue>>>());

    }

    /**
     * Creates the writer with a message header writer and a map of message
     * value writers responsible for different message values identified by
     * vendor ID and type ID to serialize RFC 6876 compliant batch elements.
     *
     * @param mHeadWriter the message header writer
     * @param valueWriters the map of message value writers
     */
    PtTlsWriter(
            final TransportWriter<PtTlsMessageHeader> mHeadWriter,
            final Map<Long, Map<Long, TransportWriter<PtTlsMessageValue>>>
                valueWriters) {
        this.mHeadWriter = mHeadWriter;
        this.valueWriters = valueWriters;
    }

    @Override
    public void write(final TransportMessage m, final ByteBuffer buffer)
            throws SerializationException {
        NotNull.check("Message cannot be null.", m);

        NotNull.check("Buffer cannot be null.", buffer);

        if (!buffer.isWriteable()) {
            throw new IllegalArgumentException("Buffer must be writeable.");
        }

        if (!(m instanceof PtTlsMessage)) {
            throw new IllegalArgumentException("Message of type "
                    + m.getClass().getCanonicalName()
                    + " is not supported. Message must be of type "
                    + PtTlsMessage.class.getCanonicalName() + ".");
        }

        PtTlsMessage message = (PtTlsMessage) m;

        /* message header */

        PtTlsMessageHeader mHead = message.getHeader();
        mHeadWriter.write(mHead, buffer);

        /* messages */
        PtTlsMessageValue value = message.getValue();

        if (value != null
                && mHead.getLength() > PtTlsMessageTlvFixedLengthEnum.MESSAGE
                        .length()) {

            long vendor = mHead.getVendorId();
            long type = mHead.getMessageType();

            if (valueWriters.containsKey(vendor)) {
                if (valueWriters.get(vendor).containsKey(type)) {
                    valueWriters.get(vendor).get(type)
                            .write(message.getValue(), buffer);
                } else {
                    throw new SerializationException(
                            "Message type is not supported.", false,
                            Long.toString(vendor), Long.toString(type));
                }
            } else {
                throw new SerializationException(
                        "Message vendor is not supported.", false,
                        Long.toString(vendor), Long.toString(type));
            }
        }

    }

    @Override
    public void add(final Long vendorId, final Long messageType,
            final TransportWriter<PtTlsMessageValue> write) {
        if (valueWriters.containsKey(vendorId)) {
            valueWriters.get(vendorId).put(messageType, write);
        } else {
            valueWriters.put(vendorId,
                    new HashMap<Long, TransportWriter<PtTlsMessageValue>>());
            valueWriters.get(vendorId).put(messageType, write);
        }

    }

    @Override
    public void remove(final Long vendorId, final Long messageType) {
        if (valueWriters.containsKey(vendorId)) {
            if (valueWriters.get(vendorId).containsKey(messageType)) {
                valueWriters.get(vendorId).remove(messageType);
            }
            if (valueWriters.get(vendorId).isEmpty()) {
                valueWriters.remove(vendorId);
            }
        }
    }

}
