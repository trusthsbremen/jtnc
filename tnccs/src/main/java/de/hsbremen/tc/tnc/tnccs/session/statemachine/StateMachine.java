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
package de.hsbremen.tc.tnc.tnccs.session.statemachine;

import java.util.List;

import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.message.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.message.tnccs.serialize.TnccsBatchContainer;
import de.hsbremen.tc.tnc.report.enums.ImHandshakeRetryReasonEnum;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.exception
.StateMachineAccessException;

/**
 * Generic TNC(C/S) session state machine to control the message flow
 * for integrity handshakes of a connection.
 *
 * @author Carl-Heinz Genzel
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
