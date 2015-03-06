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
package de.hsbremen.tc.tnc.tnccs.session.statemachine.simple;

import java.util.ArrayList;
import java.util.List;

import de.hsbremen.tc.tnc.connection.DefaultTncConnectionStateEnum;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.exception.enums.TncExceptionCodeEnum;
import de.hsbremen.tc.tnc.message.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.message.tnccs.serialize.TnccsBatchContainer;
import de.hsbremen.tc.tnc.report.enums.ImHandshakeRetryReasonEnum;
import de.hsbremen.tc.tnc.tnccs.message.handler.TncsContentHandler;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.Decided;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.End;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.State;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.StateHelper;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.StateMachine;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.enums.TnccsStateEnum;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.exception
.StateMachineAccessException;
import de.hsbremen.tc.tnc.util.NotNull;

/**
 * Default TNCS session state machine.
 *
 *
 */
public class DefaultServerStateMachine implements StateMachine {

    private State state;
    private Boolean busy;

    private Object closeLock;
    private Boolean connectionDeleteSet;

    private final StateHelper<? extends TncsContentHandler> stateHelper;

    /**
     * Creates a default TNCS session state machine with
     * the given state helper containing TNCS message handlers.
     *
     * @param stateHelper the TNCS state helper
     */
    public DefaultServerStateMachine(
            final StateHelper<? extends TncsContentHandler> stateHelper) {

        NotNull.check("StateHelper cannot be null.", stateHelper);

        this.stateHelper = stateHelper;

        this.state = null;
        this.busy = Boolean.FALSE;
        this.closeLock = new Object();
        this.connectionDeleteSet = Boolean.TRUE;
    }

    @Override
    public TnccsBatch start(final boolean selfInitiated)
            throws StateMachineAccessException {
        synchronized (this) {
            if (this.isClosed()) {
                synchronized (this.closeLock) {

                    this.busy = Boolean.TRUE;

                    this.stateHelper
                            .getHandler()
                            .setConnectionState(
                                    DefaultTncConnectionStateEnum
                                    .TNC_CONNECTION_STATE_CREATE);
                    this.connectionDeleteSet = Boolean.FALSE;

                    this.stateHelper
                            .getHandler()
                            .setConnectionState(
                                    DefaultTncConnectionStateEnum
                                    .TNC_CONNECTION_STATE_HANDSHAKE);
                    TnccsBatch b = null;

                    if (selfInitiated) {
                        this.state = this.stateHelper
                                .getState(TnccsStateEnum.INIT);
                        b = this.state.collect();
                        this.state = this.state.getConclusiveState();
                    } else {
                        this.state = this.stateHelper
                                .getState(TnccsStateEnum.CLIENT_WORKING);
                    }
                    if (this.state instanceof End) {
                        this.stop();
                    }
                    this.busy = Boolean.FALSE;

                    return b;
                }

            } else {
                throw new StateMachineAccessException(
                        "Object cannot be started, "
                        + "because it is already running.");
            }
        }

    }

    @Override
    public TnccsBatch receiveBatch(final TnccsBatchContainer newBatch)
            throws StateMachineAccessException {
        synchronized (this) {
            if (!this.isClosed() && !this.busy) {
                this.busy = Boolean.TRUE;
            } else {
                throw new StateMachineAccessException(
                        "While the object is working, "
                        + "no other messages can be received.");
            }
        }

        TnccsBatch b = null;
        synchronized (this.closeLock) {
            if (!this.isClosed()) {
                this.state = this.state.getProcessorState(newBatch.getResult());
                b = this.state.handle(newBatch);
                this.state = this.state.getConclusiveState();
                if (this.state instanceof End) {
                    this.stop();
                }
            }
        }

        this.busy = Boolean.FALSE;

        return b;

    }

    @Override
    public List<TnccsBatch> retryHandshake(
            final ImHandshakeRetryReasonEnum reason)
            throws TncException {
        synchronized (this) {
            if (!this.isClosed() && !this.busy) {
                if (this.state instanceof Decided) {
                    this.busy = Boolean.TRUE;
                } else {
                    throw new TncException("Current state "
                            + this.state.toString() + " does not allow retry.",
                            TncExceptionCodeEnum.TNC_RESULT_CANT_RETRY,
                            this.state.toString());
                }
            } else {
                throw new TncException(
                        "Retry not possible, because object ist busy.",
                        TncExceptionCodeEnum.TNC_RESULT_CANT_RETRY);
            }
        }

        List<TnccsBatch> batches = new ArrayList<>(2);
        synchronized (this.closeLock) {
            // send empty retry batch
            if (!this.isClosed()) {
                this.state = this.stateHelper.getState(TnccsStateEnum.RETRY);
                TnccsBatch b = this.state.collect();
                if (b != null) {
                    batches.add(b);
                }
                this.state = this.state.getConclusiveState();
                if (this.state instanceof End) {
                    this.stop();
                }
            }
            // send first server data batch
            if (!this.isClosed()) {
                TnccsBatch b = this.state.collect();
                if (b != null) {
                    batches.add(b);
                }
                this.state = this.state.getConclusiveState();
                if (this.state instanceof End) {
                    // only submit the close batch
                    batches.clear();
                    if (b != null) {
                        batches.add(b);
                    }
                    this.stop();
                }
            }
        }

        this.busy = Boolean.FALSE;

        return batches;
    }

    @Override
    public TnccsBatch close() throws StateMachineAccessException {
        synchronized (this) {
            if (!this.isClosed() && !this.busy) {
                this.busy = Boolean.TRUE;
            } else {
                throw new StateMachineAccessException(
                        "While the object is working, "
                        + "no other messages can be received.");
            }
        }

        TnccsBatch b = null;
        synchronized (this.closeLock) {
            if (!this.isClosed()) {
                this.state = this.stateHelper.getState(TnccsStateEnum.END);
                b = this.state.collect();
                this.state = this.state.getConclusiveState();
                if (this.state instanceof End) {
                    this.stop();
                }
            }
        }

        this.busy = Boolean.FALSE;

        return b;

    }

    @Override
    public void stop() {
        synchronized (this.closeLock) {
            this.state = (this.state instanceof End) ? this.state
                    : this.stateHelper.getState(TnccsStateEnum.END);
            if (!this.connectionDeleteSet) {
                this.stateHelper
                        .getHandler()
                        .setConnectionState(
                                DefaultTncConnectionStateEnum
                                .TNC_CONNECTION_STATE_DELETE);
                this.connectionDeleteSet = Boolean.TRUE;
            }
        }
    }

    @Override
    public boolean isClosed() {
        return (this.state == null || this.state instanceof End);
    }

    @Override
    public boolean canRetry() {
        return ((!this.isClosed() && !this.busy)
                && (this.state instanceof Decided));
    }

}
