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
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessage;

/**
 * Generic content handler base to handle all message content and message
 * exceptions. It manages different message handlers and dispatches the received
 * messages to these handlers.
 *
 *
 */
public interface TnccsContentHandler {

    /**
     * Notifies the handler that the state of the related connection has
     * changed.
     *
     * @param state the connection state
     */
    void setConnectionState(TncConnectionState state);

    /**
     * Requests the handler to ask its handlers for messages they want to
     * sent in a batch.
     * @return a list of messages
     */
    List<TnccsMessage> collectMessages();

    /**
     * Handles a list of message from a batch and returns a
     * list of messages for a new batch if necessary.
     *
     * @param list a list of messages to handle
     * @return a list of messages
     */
    List<TnccsMessage> handleMessages(List<? extends TnccsMessage> list);

    /**
     * Forwards a list of messages from a batch but does not allow
     * the handler to send messages in return.
     *
     * @param list a list of messages to dump
     */
    void dumpMessages(List<? extends TnccsMessage> list);

    /**
     * Handles a list of minor validation exceptions and returns
     * a list of error messages for a new batch if necessary.
     *
     * @param exceptions a list of exceptions
     * @return a list of messages
     */
    List<TnccsMessage> handleExceptions(List<ValidationException> exceptions);

    /**
     * Forwards a list of of minor validation exceptions but does not allow
     * the handler to send error messages in return.
     *
     * @param exceptions a list of exceptions to dump
     */
    void dumpExceptions(List<ValidationException> exceptions);

    /**
     * Requests an access decision, that is made by the contained handlers
     * based on evaluation data (e.g. result message, IMV recommendation)
     *
     * @return a connection state reflecting the access decision
     */
    TncConnectionState getAccessDecision();
}
