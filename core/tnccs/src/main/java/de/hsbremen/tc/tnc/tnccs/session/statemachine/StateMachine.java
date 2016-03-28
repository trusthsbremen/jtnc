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
package de.hsbremen.tc.tnc.tnccs.session.statemachine;

import java.util.List;

import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.message.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.message.tnccs.serialize.TnccsBatchContainer;
import de.hsbremen.tc.tnc.report.enums.ImHandshakeRetryReasonEnum;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.exception.StateMachineAccessException;

/**
 * Generic TNC(C/S) session state machine to control the message flow
 * for integrity handshakes of a connection.
 *
 *
 */
public interface StateMachine {

    /**
     * Starts the state machine and initiates the message flow. The initial
     * message flow can be different, if the connection was initiated by this
     * side or a remote peer.
     *
     * @param selfInitiated true if the connection was initiated by this side
     * @return an initial message batch or null if no initial messages
     * must be send
     * @throws StateMachineAccessException if start fails
     */
    TnccsBatch start(final boolean selfInitiated)
            throws StateMachineAccessException;

    /**
     * Sends a container with a message batch to the the state machine for
     * processing.
     *
     * @param newBatch the message batch container
     * @return a message batch or null if no messages must be send
     * @throws StateMachineAccessException if the the message batch cannot
     * be received by the state machine
     */
    TnccsBatch receiveBatch(TnccsBatchContainer newBatch)
            throws StateMachineAccessException;

    /**
     * Notifies the state machine about a handshake retry request
     * with the given reason.
     * @param reason the handshake retry reason
     * @return a list of message batches or null if no messages must be send
     * @throws TncException if retry is not possible
     */
    List<TnccsBatch> retryHandshake(
            ImHandshakeRetryReasonEnum reason) throws TncException;

    /**
     * Closes the state machine gracefully using a state
     * transition.
     *
     * @return a message batch or null if no message must be send
     * @throws StateMachineAccessException if close fails
     */
    TnccsBatch close() throws StateMachineAccessException;

    /**
     * Checks if a retry is possible, in the current state.
     *
     * @return true if retry is possible
     */
    boolean canRetry();

    /**
     * Checks if state machine is closed.
     *
     * @return true if state machine is closed
     */
    boolean isClosed();

    /**
     * Stops the state machine.
     */
    void stop();

}
