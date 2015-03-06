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
import java.util.LinkedList;
import java.util.List;

import org.ietf.nea.pb.batch.PbBatch;
import org.ietf.nea.pb.batch.PbBatchFactoryIetf;
import org.ietf.nea.pb.batch.enums.PbBatchTypeEnum;

import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessage;
import de.hsbremen.tc.tnc.message.tnccs.serialize.TnccsBatchContainer;
import de.hsbremen.tc.tnc.tnccs.message.handler.TnccContentHandler;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.AbstractState;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.ClientWorking;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.State;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.StateHelper;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.enums.TnccsStateEnum;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.simple.util.StateUtil;
import de.hsbremen.tc.tnc.util.NotNull;

/**
 * Default TNCC client working state. The TNCC handles messages
 * containing data for an integrity check handshake in the
 * client working state.
 *
 *
 */
class DefaultClientClientWorkingState extends AbstractState implements
        ClientWorking {

    private final StateHelper<TnccContentHandler> helper;

    /**
     * Creates the state with the given state helper.
     *
     * @param helper the state helper
     */
    DefaultClientClientWorkingState(
            final StateHelper<TnccContentHandler> helper) {
        this.helper = helper;
    }

    @Override
    public TnccsBatch handle(final TnccsBatchContainer batchContainer) {

        TnccsBatch b = null;

        NotNull.check("Batch cannot be null. "
                + "The state transitions seem corrupted.",
                batchContainer.getResult());

        if (batchContainer.getResult() instanceof PbBatch) {
            PbBatch current = (PbBatch) batchContainer.getResult();

            if (current.getHeader().getType().equals(
                    PbBatchTypeEnum.SDATA)) {

                try {
                    b = this.handleSdata(batchContainer);
                    super.setSuccessor(this.helper
                            .getState(TnccsStateEnum.SERVER_WORKING));

                } catch (ValidationException e) {
                    TnccsMessage error = StateUtil.createLocalError();
                    b = StateUtil.createCloseBatch(false, error);
                    super.setSuccessor(this.helper
                            .getState(TnccsStateEnum.END));
                }
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
     * Handles messages from the TNCS and returns a message batch with
     * client data.
     *
     * @param batchContainer the container containing a batch with
     * the messages from a TNCS
     * @return the message batch with client data
     * @throws ValidationException if batch creation fails
     */
    private TnccsBatch handleSdata(final TnccsBatchContainer batchContainer)
            throws ValidationException {

        PbBatch current = (PbBatch) batchContainer.getResult();

        List<? extends TnccsMessage> request = current.getMessages();
        List<TnccsMessage> response = new LinkedList<>();

        if (request != null) {

            List<TnccsMessage> msgs = this.helper.getHandler().handleMessages(
                    request);
            if (msgs != null) {
                response.addAll(msgs);
            }

        }

        if (batchContainer.getExceptions() != null) {
            List<TnccsMessage> msgs = this.helper.getHandler()
                    .handleExceptions(batchContainer.getExceptions());
            if (msgs != null) {
                response.addAll(msgs);
            }
        }

        TnccsBatch b = this.createServerBatch(response);

        return b;
    }

    /**
     * Creates a message batch of TNCC data for the TNCS.
     *
     * @param messages the messages for the batch
     * @return the message batch
     * @throws ValidationException if batch creation fails
     */
    private TnccsBatch createServerBatch(final List<TnccsMessage> messages)
            throws ValidationException {
        return PbBatchFactoryIetf
                .createClientData((messages != null) ? messages
                        : new ArrayList<TnccsMessage>(0));
    }

}
