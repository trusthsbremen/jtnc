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
package de.hsbremen.tc.tnc.im.adapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.exception.enums.TncExceptionCodeEnum;
import de.hsbremen.tc.tnc.im.adapter.data.ImComponentFactory;
import de.hsbremen.tc.tnc.im.adapter.data.ImObjectComponent;
import de.hsbremen.tc.tnc.im.adapter.data.ImRawComponent;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.attribute.ImAttribute;
import de.hsbremen.tc.tnc.message.m.serialize.ImMessageContainer;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.message.util.DefaultByteBuffer;

/**
 * Generic IM(C/V) adapter base,
 * providing common functions for derived adapters.
 * Especially important for inheritance.
 *
 *
 */
public abstract class AbstractImAdapter {

    private final ImReader<ImMessageContainer> byteReader;
    private final int messageHeaderLength;

    /**
     * Creates an adapter base with an integrity message reader object.
     * @param imReader the message reader
     */
    @SuppressWarnings("unchecked")
    protected AbstractImAdapter(
            final ImReader<? extends ImMessageContainer> imReader) {
        this.byteReader = (ImReader<ImMessageContainer>) imReader;
        this.messageHeaderLength = this.byteReader.getMinDataLength();
    }

    /**
     * Converts a raw byte message for a component
     * into an integrity message for a component.
     *
     * @param rawComponent the raw byte message
     * @return the integrity message
     * @throws TncException if a fatal exceptions is thrown while serialization
     */
    protected ImObjectComponent receiveMessage(
            final ImRawComponent rawComponent)throws TncException {
        ImMessageContainer imMessage = null;
        ImObjectComponent component = null;

        try {
            imMessage = this.byteArrayToMessage(rawComponent.getMessage());
        } catch (ValidationException e) {
            List<ValidationException> exceptions = new ArrayList<>();
            exceptions.add(e);
            component = ImComponentFactory.createFaultyObjectComponent(
                    rawComponent.getImFlags(), rawComponent.getVendorId(),
                    rawComponent.getType(), rawComponent.getCollectorId(),
                    rawComponent.getValidatorId(),
                    new ArrayList<ImAttribute>(), exceptions, Arrays.copyOf(
                            rawComponent.getMessage(), messageHeaderLength));
        }

        if (imMessage != null) {
            if (imMessage.getExceptions() == null
                    || imMessage.getExceptions().isEmpty()) {
                component = ImComponentFactory
                        .createObjectComponent(rawComponent.getImFlags(),
                                rawComponent.getVendorId(), rawComponent
                                        .getType(), rawComponent
                                        .getCollectorId(), rawComponent
                                        .getValidatorId(), (imMessage
                                        .getResult() != null) ? imMessage
                                        .getResult().getAttributes()
                                        : new ArrayList<ImAttribute>());
            } else {
                component = ImComponentFactory
                        .createFaultyObjectComponent(rawComponent.getImFlags(),
                                rawComponent.getVendorId(), rawComponent
                                        .getType(), rawComponent
                                        .getCollectorId(), rawComponent
                                        .getValidatorId(), (imMessage
                                        .getResult() != null) ? imMessage
                                        .getResult().getAttributes()
                                        : new ArrayList<ImAttribute>(),
                                imMessage.getExceptions(), Arrays.copyOf(
                                        rawComponent.getMessage(),
                                        messageHeaderLength));
            }
        } else {
            throw new TncException(
                    "Message was null after parsing, this should never happen.",
                    TncExceptionCodeEnum.TNC_RESULT_FATAL);
        }

        return component;
    }

    /**
     * Converts a raw byte message into an integrity message object in
     * a container.
     *
     * @param message the raw byte message
     * @return the integrity message container
     * @throws TncException if a fatal exceptions is thrown while serialization
     * @throws ValidationException if validation of a message value fails
     */
    private ImMessageContainer byteArrayToMessage(final byte[] message)
            throws TncException, ValidationException {
        ImMessageContainer imMessage = null;
        try {
            ByteBuffer buf = new DefaultByteBuffer(message.length);
            buf.write(message);
            imMessage = this.byteReader.read(buf, message.length);
        } catch (SerializationException e) {
            throw new TncException(e.getMessage(),
                    TncExceptionCodeEnum.TNC_RESULT_OTHER);
        }

        return imMessage;
    }

}
