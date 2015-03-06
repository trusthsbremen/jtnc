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
