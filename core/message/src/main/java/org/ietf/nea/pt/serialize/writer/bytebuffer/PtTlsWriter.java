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
