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
