/**
 * The BSD 3-Clause License ("BSD New" or "BSD Simplified")
 *
 * Copyright (c) 2015, Carl-Heinz Genzel
 * All rights reserved.
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
package de.hsbremen.tc.tnc.im.session;

import de.hsbremen.tc.tnc.connection.TncConnectionState;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.im.adapter.data.ImObjectComponent;
import de.hsbremen.tc.tnc.im.session.enums.ImMessageTriggerEnum;

/**
 * Generic session base. The session manages an IM(C/V) connection and is
 * used to communicate thru the underlying connection.
 *
 *
 */
interface ImSession {

    // TODO use custom exceptions where possible.

    /**
     * Returns the current connection state. (e.g. closed)
     *
     * @return the connection state.
     */
    TncConnectionState getConnectionState();

    /**
     * Sets the connection state to a new state.
     *
     * @param imConnectionState the new state
     */
    void setConnectionState(
            TncConnectionState imConnectionState);

    /**
     * Requests integrity measurement messages. Based on the current
     * state of the message retrieval (e.g. handshake begin, batch end).
     *
     * @param trigger the message retrieval state
     * @throws TncException if message retrieval fails
     */
    void triggerMessage(ImMessageTriggerEnum trigger)
            throws TncException;

    /**
     * Handles integrity measurement component messages and returns
     * a response if necessary.
     *
     * @param <T> the component representation type
     * @param component the component message
     * @throws TncException if message handling fails
     */
    <T extends ImObjectComponent> void handleMessage(T component)
            throws TncException;

    /**
     * Terminates the session.
     */
    void terminate();

}
