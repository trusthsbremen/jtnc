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
package de.hsbremen.tc.tnc.tnccs.message.handler;

import java.util.List;

import de.hsbremen.tc.tnc.connection.TncConnectionState;
import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessage;

/**
 * Generic handler base to handle and collect IF-TNCCS messages.
 *
 *
 */
interface TnccsMessageHandler {

    /*
     * Maybe this method is needed to determine where to send the messages and
     * where not, this however could be filtered by the handler too.
     *
     * public abstract Map<Long,Set<Long>> getForwardableMessageTypes();
     */

    /**
     * Notifies the handler that the state of the related connection has
     * changed.
     *
     * @param state the connection state
     */
    void setConnectionState(TncConnectionState state);

    /**
     * Requests a list of messages the handler wants to send.
     * @return a list of messages
     */
    List<TnccsMessage> requestMessages();

    /**
     * Handles a message and returns a list of messages if necessary.
     *
     * @param message the message to handle
     * @return a list of messages
     */
    List<TnccsMessage> forwardMessage(TnccsMessage message);

    /**
     * Notifies the handler that all already collected messages will
     * be send. The handler can send further messages if necessary.
     *
     * @return a list of messages
     */
    List<TnccsMessage> lastCall();

    /**
     * Forwards the messages to the handler but does not allow
     * the handler to send messages in return.
     *
     * @param message the message to dump
     */
    void dumpMessage(TnccsMessage message);
}
