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
package org.ietf.nea.pt.serialize.reader.bytebuffer;

import java.nio.BufferUnderflowException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.ietf.nea.pt.message.DefaultTransportMessageContainer;
import org.ietf.nea.pt.message.PtTlsMessage;
import org.ietf.nea.pt.message.PtTlsMessageHeader;
import org.ietf.nea.pt.message.enums.PtTlsMessageTlvFixedLengthEnum;
import org.ietf.nea.pt.validate.enums.PtTlsErrorCauseEnum;
import org.ietf.nea.pt.validate.rules.MinMessageLength;
import org.ietf.nea.pt.value.PtTlsMessageValue;
import org.ietf.nea.pt.value.enums.PtTlsMessageErrorCodeEnum;

import de.hsbremen.tc.tnc.message.Combined;
import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.t.serialize.TransportMessageContainer;
import de.hsbremen.tc.tnc.message.t.serialize.bytebuffer.TransportReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.message.util.DefaultByteBuffer;
import de.hsbremen.tc.tnc.util.NotNull;

/**
 * Reader to parse an entire transport message compliant to RFC 6876 from a
 * buffer of bytes to a Java object. The reader is composed of several
 * readers, that are used to parse the different batch elements (e.g. header,
 * values, ...). Message value readers can be managed with the add and remove
 * operations.
 *
 *
 */
public class PtTlsReader implements TransportReader<TransportMessageContainer>,
        Combined<TransportReader<PtTlsMessageValue>> {

    private final TransportReader<PtTlsMessageHeader> mHeadReader;
    private final Map<Long, Map<Long, TransportReader<PtTlsMessageValue>>>
        valueReaders;

    /**
     * Creates the reader with a message header reader to parse RFC 6876
     * compliant headers.
     *
     * @param mHeadReader the message header reader
     */
    PtTlsReader(final TransportReader<PtTlsMessageHeader> mHeadReader) {
        this(mHeadReader,
                new HashMap<Long, Map<Long,
                TransportReader<PtTlsMessageValue>>>());
    }

    /**
     * Creates the reader with a message header reader and a map of message
     * value readers responsible for different message values identified by
     * vendor ID and type ID to parse RFC 6876 compliant message elements.
     *
     * @param mHeadReader the message header reader
     * @param valueReaders the map of message value readers
     */
    PtTlsReader(
            final TransportReader<PtTlsMessageHeader> mHeadReader,
            final Map<Long, Map<Long, TransportReader<PtTlsMessageValue>>>
                valueReaders) {
        this.mHeadReader = mHeadReader;
        this.valueReaders = valueReaders;
    }

    @Override
    public TransportMessageContainer read(final ByteBuffer buffer,
            final long length) throws SerializationException,
            ValidationException {

        NotNull.check("Buffer cannot be null.", buffer);

        if (!buffer.isReadable()) {
            throw new IllegalArgumentException("Buffer must be readable.");
        }

        List<ValidationException> minorExceptions = new LinkedList<>();

        /* Message header */
        PtTlsMessageHeader mHead = null;

        ByteBuffer fullBuffer = null;

        byte[] headerByteCopy = buffer.read(this.getMinDataLength());

        if (headerByteCopy.length < this.getMinDataLength()) {
            throw new SerializationException(
                    "Buffer does not contain enough data.", true,
                    headerByteCopy.length);
        }

        ByteBuffer tempBuf = new DefaultByteBuffer(headerByteCopy.length);
        tempBuf.write(headerByteCopy);

        // ignore length here, because header has a length field.
        try {
            mHead = mHeadReader.read(tempBuf, -1);
        } catch (ValidationException e) {
            throw new ValidationException(e.getMessage(), e.getCause(),
                    e.getExceptionOffset(), headerByteCopy);
        }

        try {
            if (buffer.isReadable() && !buffer.isWriteable()) {
                fullBuffer = new DefaultByteBuffer(mHead.getLength());
                fullBuffer.write(headerByteCopy); // copy the header data back
                                                  // in
                fullBuffer.write(buffer.read(mHead.getLength()
                        - this.getMinDataLength())); // get the rest of the data
                fullBuffer.read(headerByteCopy.length); // set correct position
            } else {
                fullBuffer = buffer;
            }

        } catch (BufferUnderflowException e) {
            throw new SerializationException("Buffer capacity "
                    + buffer.capacity() + " to short.", e, false,
                    Long.toString(buffer.capacity()));
        }

        /* Value */
        long vendor = mHead.getVendorId();
        long type = mHead.getMessageType();

        PtTlsMessageValue mValue = null;

        if (valueReaders.containsKey(vendor)) {
            if (valueReaders.get(vendor).containsKey(type)) {
                TransportReader<PtTlsMessageValue> vr = valueReaders.get(vendor)
                        .get(type);

                // Do a length check before trying to parse the value.
                long valueLength = mHead.getLength()
                        - PtTlsMessageTlvFixedLengthEnum.MESSAGE.length();

                try {
                    MinMessageLength.check(valueLength, vr.getMinDataLength());
                } catch (RuleException e1) {
                    // Remove 8 from header offset because this is a late header
                    // check. Length is the second last field
                    // in the message header.
                    throw new ValidationException(
                            e1.getMessage(),
                            e1,
                            PtTlsMessageTlvFixedLengthEnum.MESSAGE.length() - 8,
                            headerByteCopy);
                }

                try {
                    mValue = vr.read(fullBuffer, valueLength);
                } catch (ValidationException e) {
                    throw new ValidationException(e.getMessage(), e.getCause(),
                            e.getExceptionOffset(), headerByteCopy);
                }

            } else {
                throw new ValidationException(
                        "Message type is not supported.",
                        new RuleException(
                                "Message with vendor ID " + vendor
                                        + " and type " + type
                                        + " not supported.",
                                false,
                                PtTlsMessageErrorCodeEnum
                                    .IETF_UNSUPPORTED_MESSAGE_TYPE.code(),
                                PtTlsErrorCauseEnum
                                    .MESSAGE_TYPE_NOT_SUPPORTED.id()),
                                    4, headerByteCopy,
                        Long.toString(vendor), Long.toString(type));
            }
        } else {
            throw new ValidationException(
                    "Message type is not supported.",
                    new RuleException(
                            "Message with vendor ID " + vendor + " and type "
                                    + type + " not supported.",
                            false,
                            PtTlsMessageErrorCodeEnum
                                .IETF_UNSUPPORTED_MESSAGE_TYPE.code(),
                            PtTlsErrorCauseEnum
                                .MESSAGE_TYPE_NOT_SUPPORTED.id()),
                    1, headerByteCopy, Long.toString(vendor), Long
                            .toString(type));
        }

        PtTlsMessage m = new PtTlsMessage(mHead, mValue);

        return new DefaultTransportMessageContainer(m, minorExceptions);

    }

    @Override
    public byte getMinDataLength() {
        // message header is minimal requirement
        return mHeadReader.getMinDataLength();
    }

    @Override
    public void add(final Long vendorId, final Long messageType,
            final TransportReader<PtTlsMessageValue> reader) {
        if (valueReaders.containsKey(vendorId)) {
            valueReaders.get(vendorId).put(messageType, reader);
        } else {
            valueReaders.put(vendorId,
                    new HashMap<Long, TransportReader<PtTlsMessageValue>>());
            valueReaders.get(vendorId).put(messageType, reader);
        }

    }

    @Override
    public void remove(final Long vendorId, final Long messageType) {
        if (valueReaders.containsKey(vendorId)) {
            if (valueReaders.get(vendorId).containsKey(messageType)) {
                valueReaders.get(vendorId).remove(messageType);
            }
            if (valueReaders.get(vendorId).isEmpty()) {
                valueReaders.remove(vendorId);
            }
        }
    }

}
