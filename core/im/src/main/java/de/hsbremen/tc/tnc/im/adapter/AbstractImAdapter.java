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
                    rawComponent.getType(), rawComponent.getSourceId(),
                    rawComponent.getDestinationId(),
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
                                        .getSourceId(), rawComponent
                                        .getDestinationId(), (imMessage
                                        .getResult() != null) ? imMessage
                                        .getResult().getAttributes()
                                        : new ArrayList<ImAttribute>());
            } else {
                component = ImComponentFactory
                        .createFaultyObjectComponent(rawComponent.getImFlags(),
                                rawComponent.getVendorId(), rawComponent
                                        .getType(), rawComponent
                                        .getSourceId(), rawComponent
                                        .getDestinationId(), (imMessage
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
