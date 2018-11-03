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
package de.hsbremen.tc.tnc.tnccs.session.statemachine.simple;

import org.ietf.nea.pb.batch.PbBatch;
import org.ietf.nea.pb.batch.enums.PbBatchTypeEnum;

import de.hsbremen.tc.tnc.connection.DefaultTncConnectionStateEnum;
import de.hsbremen.tc.tnc.connection.TncConnectionState;
import de.hsbremen.tc.tnc.message.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.message.tnccs.serialize.TnccsBatchContainer;
import de.hsbremen.tc.tnc.tnccs.message.handler.TnccContentHandler;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.AbstractState;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.Decided;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.State;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.StateHelper;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.enums.TnccsStateEnum;
import de.hsbremen.tc.tnc.util.NotNull;

/**
 * Default TNCC decided state. The TNCC handles
 * results from the TNCS and waits for handshake retry requests
 * in the decided state.
 *
 *
 */
class DefaultClientDecidedState extends AbstractState implements Decided {

    private final StateHelper<TnccContentHandler> helper;

    /**
     * Creates the state with the given state helper.
     *
     * @param helper the state helper
     */
    DefaultClientDecidedState(
            final StateHelper<TnccContentHandler> helper) {

        this.helper = helper;
    }

    @Override
    public State getProcessorState(final TnccsBatch result) {
        if (result != null && result instanceof PbBatch) {
            PbBatch b = (PbBatch) result;

            if (b.getHeader().getType().equals(PbBatchTypeEnum.SRETRY)) {
                this.helper.getHandler().setConnectionState(
                        DefaultTncConnectionStateEnum.TNC_CONNECTION_STATE_HANDSHAKE);

                return this.helper.getState(TnccsStateEnum.SERVER_WORKING);
            }

            if (b.getHeader().getType().equals(PbBatchTypeEnum.CLOSE)) {

                return this.helper.getState(TnccsStateEnum.END);
            }

        }

        return this.helper.getState(TnccsStateEnum.ERROR);
    }

    @Override
    public TnccsBatch handle(final TnccsBatchContainer batchContainer) {
        TnccsBatch b = null;

        NotNull.check("Batch cannot be null. "
                + "The state transitions seem corrupted.",
                batchContainer.getResult());

        if (batchContainer.getResult() instanceof PbBatch) {
            PbBatch current = (PbBatch) batchContainer.getResult();

            if (current.getHeader().getType()
                    .equals(PbBatchTypeEnum.RESULT)) {
                this.handleResult(batchContainer);
                super.setSuccessor(this);
            } else {

                throw new IllegalArgumentException(
                                "Batch cannot be of type "
                                + current.getHeader().getType().toString()
                                + ". The state transitions seem corrupted."
                                );

            }
        } else {

            throw new IllegalArgumentException(
                    "Batch must be an instance of "
                            + PbBatch.class.getCanonicalName()
                            + ". The state transitions seem corrupted.");

        }

        return b;
    }

    @Override
    public State getConclusiveState() {
        State s = super.getSuccessor();
        super.setSuccessor(null);

        return s;
    }

    /**
     * Process a result from the TNCS and changes the connection
     * state of the session according to the result.
     *
     * @param batchContainer the container containing a batch with
     * a result message from the TNCS
     */
    private void handleResult(final TnccsBatchContainer batchContainer) {
        PbBatch b = (PbBatch) batchContainer.getResult();
        if (b.getMessages() != null) {
            this.helper.getHandler().dumpMessages(b.getMessages());
        }

        if (batchContainer.getExceptions() != null) {
            this.helper.getHandler().dumpExceptions(
                    batchContainer.getExceptions());
        }

        TncConnectionState state = this.helper.getHandler()
                .getAccessDecision();

        if (state.equals(DefaultTncConnectionStateEnum
                .TNC_CONNECTION_STATE_ACCESS_ALLOWED)
                || state.equals(DefaultTncConnectionStateEnum
                        .TNC_CONNECTION_STATE_ACCESS_ISOLATED)
                || state.equals(DefaultTncConnectionStateEnum
                        .TNC_CONNECTION_STATE_ACCESS_NONE)) {

            this.helper.getHandler().setConnectionState(state);

        } else {

            throw new IllegalStateException("State " + state.toString()
                    + " does not reflect access decision");

        }

    }

}
