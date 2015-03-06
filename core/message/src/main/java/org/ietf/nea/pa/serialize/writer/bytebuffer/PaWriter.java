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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ietf.nea.pa.attribute.PaAttribute;
import org.ietf.nea.pa.attribute.PaAttributeHeader;
import org.ietf.nea.pa.attribute.PaAttributeValue;
import org.ietf.nea.pa.message.PaMessage;
import org.ietf.nea.pa.message.PaMessageHeader;

import de.hsbremen.tc.tnc.message.Combined;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.m.message.ImMessage;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.util.NotNull;

/**
 * Writer to serialize an entire integrity measurement component message
 * compliant to RFC 5792 from a Java object to a buffer of bytes. The writer is
 * composed of several writers that are used to serialize the different message
 * elements (e.g. header, attributes, ...). Attribute value writers can be
 * managed with the add and remove operations.
 *
 *
 */
public class PaWriter implements ImWriter<ImMessage>,
        Combined<ImWriter<PaAttributeValue>> {

    private final ImWriter<PaMessageHeader> mHeadWriter;
    private final ImWriter<PaAttributeHeader> aHeadWriter;
    private final Map<Long, Map<Long, ImWriter<PaAttributeValue>>> valueWriters;

    /**
     * Creates the writer with a message header writer and an attribute header
     * writer to serialize RFC 5792 compliant headers.
     *
     * @param mHeadWriter the message header writer
     * @param aHeadWriter the attribute header writer
     */
    PaWriter(final ImWriter<PaMessageHeader> mHeadWriter,
            final ImWriter<PaAttributeHeader> aHeadWriter) {
        this(mHeadWriter, aHeadWriter,
                new HashMap<Long, Map<Long, ImWriter<PaAttributeValue>>>());

    }

    /**
     * Creates the writer with a message header writer, an attribute header
     * writer and a map of attribute value writers responsible for different
     * attribute values identified by vendor ID and type ID to serialize RFC
     * 5792 compliant message elements.
     *
     * @param mHeadWriter the message header writer
     * @param aHeadWriter the attribute header writer
     * @param valueWriters the map of attribute value writers
     */
    PaWriter(final ImWriter<PaMessageHeader> mHeadWriter,
            final ImWriter<PaAttributeHeader> aHeadWriter,
            final Map<Long, Map<Long, ImWriter<PaAttributeValue>>>
                valueWriters) {
        this.mHeadWriter = mHeadWriter;
        this.aHeadWriter = aHeadWriter;
        this.valueWriters = valueWriters;
    }

    @Override
    public void write(final ImMessage message, final ByteBuffer buffer)
            throws SerializationException {
        NotNull.check("Message cannot be null.", message);

        NotNull.check("Buffer cannot be null.", buffer);

        if (!buffer.isWriteable()) {
            throw new IllegalArgumentException("Buffer must be writeable.");
        }

        if (!(message instanceof PaMessage)) {
            throw new IllegalArgumentException("Message of type "
                    + message.getClass().getCanonicalName()
                    + " is not supported. Message must be of type "
                    + PaMessage.class.getCanonicalName() + ".");
        }

        PaMessage paMessage = (PaMessage) message;

        /* message header */

        PaMessageHeader mHead = paMessage.getHeader();
        mHeadWriter.write(mHead, buffer);

        /* attributes */
        List<PaAttribute> attributes = paMessage.getAttributes();
        if (attributes != null) {
            for (PaAttribute paAttribute : attributes) {

                PaAttributeHeader aHead = paAttribute.getHeader();
                long vendor = aHead.getVendorId();
                long type = aHead.getAttributeType();

                if (valueWriters.containsKey(vendor)) {
                    if (valueWriters.get(vendor).containsKey(type)) {
                        aHeadWriter.write(aHead, buffer);
                        valueWriters.get(vendor).get(type)
                                .write(paAttribute.getValue(), buffer);
                    } else {
                        throw new SerializationException(
                                "Attribute type is not supported.", false,
                                Long.toString(vendor), Long.toString(type));
                    }
                } else {
                    throw new SerializationException(
                            "Attribute vendor is not supported.", false,
                            Long.toString(vendor), Long.toString(type));
                }
            }
        }

    }

    @Override
    public void add(final Long vendorId, final Long messageType,
            final ImWriter<PaAttributeValue> writer) {
        if (valueWriters.containsKey(vendorId)) {
            valueWriters.get(vendorId).put(messageType, writer);
        } else {
            valueWriters.put(vendorId,
                    new HashMap<Long, ImWriter<PaAttributeValue>>());
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
