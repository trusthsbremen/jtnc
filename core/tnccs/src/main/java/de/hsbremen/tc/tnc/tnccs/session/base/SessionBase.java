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
package de.hsbremen.tc.tnc.tnccs.session.base;

import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.exception.ComprehensibleException;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.StateMachine;
import de.hsbremen.tc.tnc.transport.TransportConnection;

/**
 * Generic TNC(C/S) session base. It contains the basic functionality
 * to manage a session.
 *
 *
 */
public interface SessionBase {

    /**
     * Registers a state machine to be used by the session
     * for the integrity handshake and connection management.
     * This method is used to initialize the session properly
     * and can only be called once.
     *
     * @param machine the state machine
     */
    void registerStatemachine(StateMachine machine);

    /**
     * Registers a transport connection for message transmission
     * with the session.
     * This method is used to initialize the session properly
     * and can only be called once.
     *
     * @param connection the related transport connection
     */
    void registerConnection(TransportConnection connection);

    /**
     * Starts the session and initializes its management functions.
     * @param selfInitiated true if the underlying connection
     * was initiated by this side and not by a remote peer
     */
    void start(boolean selfInitiated);

    /**
     * If a exceptions with further objects to assess the
     * reason occurred, it can be handled by the session.
     * @param e the exception
     */
    void handle(ComprehensibleException e);

    /**
     * Closes a session.
     */
    void close();

    /**
     * Returns if the session is closed.
     * @return true if session is closed
     */
    boolean isClosed();

    /**
     * Returns the session attributes.
     * @return the session attributes
     */
    Attributed getAttributes();

}
