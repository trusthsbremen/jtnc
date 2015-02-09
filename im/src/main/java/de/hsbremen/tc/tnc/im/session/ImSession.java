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
package de.hsbremen.tc.tnc.im.session;

import de.hsbremen.tc.tnc.connection.TncConnectionState;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.im.adapter.data.ImObjectComponent;
import de.hsbremen.tc.tnc.im.session.enums.ImMessageTriggerEnum;

/**
 * Generic session base. The session manages a IM(C/V) connection and is used to
 * communicate thru the underlying connection.
 *
 * @author Carl-Heinz Genzel
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
