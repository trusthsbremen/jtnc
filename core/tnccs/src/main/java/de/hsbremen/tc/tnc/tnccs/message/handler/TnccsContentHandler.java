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
