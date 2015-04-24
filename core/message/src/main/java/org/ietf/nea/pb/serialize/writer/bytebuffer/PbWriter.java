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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ietf.nea.pb.batch.PbBatch;
import org.ietf.nea.pb.batch.PbBatchHeader;
import org.ietf.nea.pb.message.PbMessage;
import org.ietf.nea.pb.message.PbMessageHeader;
import org.ietf.nea.pb.message.PbMessageValue;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLengthEnum;

import de.hsbremen.tc.tnc.message.Combined;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.message.tnccs.serialize.bytebuffer.TnccsWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.util.NotNull;

/**
 * Writer to serialize an entire TNCCS batch compliant to RFC 5793 from a Java
 * object to a buffer of bytes. The writer is composed of several writers that,
 * are used to serialize the different batch elements (e.g. header, message ,
 * ...). Message value writers can be managed with the add and remove
 * operations.
 *
 *
 */
public class PbWriter implements TnccsWriter<TnccsBatch>,
        Combined<TnccsWriter<PbMessageValue>> {

    private final TnccsWriter<PbBatchHeader> bHeadWriter;
    private final TnccsWriter<PbMessageHeader> mHeadWriter;
    private final Map<Long, Map<Long, TnccsWriter<PbMessageValue>>>
        valueWriters;

    /**
     * Creates the writer with a batch header writer and a message header
     * writer to serialize RFC 5793 compliant headers.
     *
     * @param bHeadWriter the batch header writer
     * @param mHeadWriter the message header writer
     */
    PbWriter(final TnccsWriter<PbBatchHeader> bHeadWriter,
            final TnccsWriter<PbMessageHeader> mHeadWriter) {
        this(bHeadWriter, mHeadWriter,
                new HashMap<Long, Map<Long, TnccsWriter<PbMessageValue>>>());

    }

    /**
     * Creates the writer with a batch header writer, a message header writer
     * and a map of message value writers responsible for different message
     * values identified by vendor ID and type ID to serialize RFC 5793
     * compliant batch elements.
     *
     * @param bHeadWriter the batch header writer
     * @param mHeadWriter the message header writer
     * @param valueWriters the map of message value writers
     */
    PbWriter(final TnccsWriter<PbBatchHeader> bHeadWriter,
            final TnccsWriter<PbMessageHeader> mHeadWriter,
            final Map<Long, Map<Long, TnccsWriter<PbMessageValue>>>
                valueWriters) {
        this.bHeadWriter = bHeadWriter;
        this.mHeadWriter = mHeadWriter;
        this.valueWriters = valueWriters;
    }

    @Override
    public void write(final TnccsBatch batch, final ByteBuffer buffer)
            throws SerializationException {
        NotNull.check("Batch cannot be null.", batch);

        NotNull.check("Output buffer cannot be null.", buffer);

        if (!buffer.isWriteable()) {
            throw new IllegalArgumentException("Buffer must be writeable.");
        }

        if (!(batch instanceof PbBatch)) {
            throw new IllegalArgumentException("Batch of type "
                    + batch.getClass().getCanonicalName()
                    + " is not supported. Bacth must be of type "
                    + PbBatch.class.getCanonicalName() + ".");
        }

        PbBatch pbBatch = (PbBatch) batch;

        /* batch header */

        PbBatchHeader bHead = pbBatch.getHeader();
        bHeadWriter.write(bHead, buffer);

        /* messages */
        List<PbMessage> msgs = pbBatch.getMessages();
        if (msgs != null
                && bHead.getLength() > PbMessageTlvFixedLengthEnum.BATCH
                        .length()) {
            for (PbMessage pbMessage : msgs) {

                PbMessageHeader mHead = pbMessage.getHeader();
                long vendor = mHead.getVendorId();
                long type = mHead.getMessageType();

                if (valueWriters.containsKey(vendor)) {
                    if (valueWriters.get(vendor).containsKey(type)) {
                        mHeadWriter.write(mHead, buffer);
                        valueWriters.get(vendor).get(type)
                                .write(pbMessage.getValue(), buffer);
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
        
        
    }

    @Override
    public void add(final Long vendorId, final Long messageType,
            final TnccsWriter<PbMessageValue> writer) {
        if (valueWriters.containsKey(vendorId)) {
            valueWriters.get(vendorId).put(messageType, writer);
        } else {
            valueWriters.put(vendorId,
                    new HashMap<Long, TnccsWriter<PbMessageValue>>());
            valueWriters.get(vendorId).put(messageType, writer);
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
