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
package de.hsbremen.tc.tnc.tnccs.adapter.connection;

import java.util.List;

import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessage;
import de.hsbremen.tc.tnc.report.enums.HandshakeRetryReasonEnum;

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
    void requestHandshakeRetry(HandshakeRetryReasonEnum reason)
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
