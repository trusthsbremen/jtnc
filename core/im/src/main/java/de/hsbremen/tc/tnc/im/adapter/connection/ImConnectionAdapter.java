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
package de.hsbremen.tc.tnc.im.adapter.connection;

import de.hsbremen.tc.tnc.attribute.TncAttributeType;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.im.adapter.data.ImObjectComponent;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.report.enums.ImHandshakeRetryReasonEnum;

/**
 * Generic adapter base for an IM(C/V) connection.
 *
 * @author Carl-Heinz Genzel
 */
public interface ImConnectionAdapter {

    /**
     * Sends a integrity measurement component message from the IM(C/V) to the
     * lower layers.
     *
     * @param component the component message
     * @param identifier the connection unique identifier for the message
     * @throws TncException if the transmission fails
     * @throws ValidationException if the serialization for the transmission
     * fails
     */
    void sendMessage(ImObjectComponent component,
            long identifier) throws TncException, ValidationException;

    /**
     * Request handshake for the connection with the given reason.
     * @param reason the reason for the request
     * @throws TncException if handshake is not acceptable
     */
    void requestHandshakeRetry(ImHandshakeRetryReasonEnum reason)
            throws TncException;

    /**
     * Get a connection attribute by the give type.
     * @param type the attribute type
     * @return the attribute
     * @throws TncException if the attribute was not accessible
     */
    Object getAttribute(TncAttributeType type)
            throws TncException;
}
