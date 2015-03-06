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
package de.hsbremen.tc.tnc.tnccs.adapter.connection;

import java.util.List;

import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessage;
import de.hsbremen.tc.tnc.report.enums.ImHandshakeRetryReasonEnum;

/**
 * Generic connection context base. The context provides common attributes and
 * function for all connections of a session. It enables the connection to
 * interact with the related session.
 *
 *
 */
interface ImConnectionContext extends Attributed {

    /**
     * Adds a message received by the connection to the context
     * for transmission to a the peer.
     *
     * @param message the message
     * @throws TncException if message cannot be added to the context
     */
    void addMessage(TnccsMessage message) throws TncException;

    /**
     * Returns all messages, which were added to the context and
     * clears the context message buffer.
     *
     * @return a list of messages
     */
    List<TnccsMessage> clearMessage();

    /**
     * Requests a handshake retry at the session.
     *
     * @param reason the retry reason
     * @throws TncException if handshake retry fails
     */
    void requestHandshakeRetry(ImHandshakeRetryReasonEnum reason)
            throws TncException;

    /**
     * Enables a context and its functions.
     */
    void validate();

    /**
     * Disables a context and its functions.
     */
    void invalidate();

    /**
     * Checks if context is enabled and functions
     * are usable.
     *
     * @return true if context is enabled (valid)
     */
    boolean isValid();

}
