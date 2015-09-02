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
package de.hsbremen.tc.tnc.tnccs.adapter.im;

import de.hsbremen.tc.tnc.connection.TncConnectionState;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessageValue;
import de.hsbremen.tc.tnc.tnccs.adapter.im.exception.TerminatedException;

/**
 * Generic base adapter for an IM(C/V).
 *
 *
 * @param <T> the connection type (e.g. IMC or IMV connection)
 */
public interface ImAdapter<T> {

    /**
     * Returns the primary ID of an IM(C/V).
     *
     * @return the primary ID
     */
    long getPrimaryId();

    /**
     * Notifies an IM(C/V) that the state of the given connection has changed.
     *
     * @param connection the connection
     * @param state the new connection state
     * @throws TncException if notification fails
     * @throws TerminatedException if IM(C/V) is terminated
     */
    void notifyConnectionChange(T connection, TncConnectionState state)
            throws TncException, TerminatedException;

    /**
     * Notifies the IM(C/V) that a integrity check handshake has begun on the
     * given connection.
     *
     * @param connection the connection
     * @throws TncException if notification fails
     * @throws TerminatedException if IM(C/V) is terminated
     */
    void beginHandshake(T connection) throws TncException,
            TerminatedException;

    /**
     * Handles a message from a batch received for the given connection.
     *
     * @param connection the connection
     * @param message the message to handle
     * @throws TncException if handling fails
     * @throws TerminatedException if IM(C/V) is terminated
     */
    void handleMessage(T connection, TnccsMessageValue message)
            throws TncException, TerminatedException;

    /**
     * Notifies the IM(C/V) that the last message of a batch
     * has been delivered for the given connection. An IM(C/V)
     * can sent further messages if necessary.
     *
     * @param connection the connection
     * @throws TncException if notification fails
     * @throws TerminatedException if IM(C/V) is terminated
     */
    void batchEnding(T connection) throws TncException,
            TerminatedException;

    /**
     * Terminates the IM(C/V).
     * @throws TerminatedException if IM(C/V) is already terminated
     */
    void terminate() throws TerminatedException;
}
