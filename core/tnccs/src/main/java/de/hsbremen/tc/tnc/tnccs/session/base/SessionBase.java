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
