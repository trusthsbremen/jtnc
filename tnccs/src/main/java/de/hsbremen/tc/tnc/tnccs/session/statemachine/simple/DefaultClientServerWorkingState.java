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

import org.ietf.nea.pb.batch.PbBatch;
import org.ietf.nea.pb.batch.enums.PbBatchTypeEnum;

import de.hsbremen.tc.tnc.message.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.message.tnccs.serialize.TnccsBatchContainer;
import de.hsbremen.tc.tnc.tnccs.message.handler.TnccContentHandler;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.AbstractState;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.ServerWorking;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.State;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.StateHelper;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.enums.TnccsStateEnum;

/**
 * Default TNCC server working state. The TNCC listens
 * for further messages in the server working state and changes
 * the state according to a newly received message.
 *
 * @author Carl-Heinz Genzel
 *
 */
class DefaultClientServerWorkingState extends AbstractState implements
        ServerWorking {

    private final StateHelper<TnccContentHandler> helper;

    /**
     * Creates the state with the given state helper.
     *
     * @param helper the state helper
     */
    DefaultClientServerWorkingState(
            final StateHelper<TnccContentHandler> helper) {

        this.helper = helper;
    }

    @Override
    public State getProcessorState(final TnccsBatch result) {

        if (result != null && result instanceof PbBatch) {
            PbBatch b = (PbBatch) result;

            if (b.getHeader().getType().equals(PbBatchTypeEnum.SDATA)) {

                return this.helper.getState(TnccsStateEnum.CLIENT_WORKING);
            }

            if (b.getHeader().getType().equals(PbBatchTypeEnum.RESULT)) {

                return this.helper.getState(TnccsStateEnum.DECIDED);
            }

            if (b.getHeader().getType().equals(PbBatchTypeEnum.CLOSE)) {

                return this.helper.getState(TnccsStateEnum.END);
            }

            if (b.getHeader().getType().equals(PbBatchTypeEnum.SRETRY)) {
                super.setSuccessor(null);
                return this;
            }

        }

        return this.helper.getState(TnccsStateEnum.ERROR);

    }

    @Override
    public TnccsBatch handle(final TnccsBatchContainer batchContainer) {

        TnccsBatch b = null;

        if (batchContainer.getResult() == null) {
            throw new NullPointerException(
                    "Batch cannot be null. "
                    + "The state transitions seem corrupted.");

        } else {

            if (batchContainer.getResult() instanceof PbBatch) {
                PbBatch current = (PbBatch) batchContainer.getResult();

                if (current.getHeader().getType()
                        .equals(PbBatchTypeEnum.SRETRY)) {
                    // this is redundant and can be ignored, wait for next
                    // message
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
        }

        return b;
    }

    @Override
    public State getConclusiveState() {
        State s = super.getSuccessor();
        super.setSuccessor(null);

        return s;
    }

}
