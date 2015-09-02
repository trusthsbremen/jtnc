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
