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
import de.hsbremen.tc.tnc.tnccs.message.handler.TnccsContentHandler;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.AbstractState;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.End;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.State;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.StateHelper;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.simple.util.StateUtil;
import de.hsbremen.tc.tnc.util.NotNull;

/**
 * Default common end state. The TNC(C/S) handles a close messages
 * in the end state if one was received or creates one if an
 * external event to close the connection occurred.
 *
 * @author Carl-Heinz Genzel
 *
 */
class DefaultCommonEndState extends AbstractState implements End {

    private final boolean server;
    private final StateHelper<? extends TnccsContentHandler> helper;

    /**
     * Creates the state with the given state helper.
     *
     * @param server true if the state is used within a TNCS
     * @param helper the state helper
     *
     */
    DefaultCommonEndState(final boolean server,
            final StateHelper<? extends TnccsContentHandler> helper) {

        this.server = server;
        this.helper = helper;
    }

    @Override
    public State getProcessorState(final TnccsBatch result) {
        // if getProcessor is invoked, the EndState was already
        // handled once, so set the successor to reference the end state
        // itself to prevent the EndState from handling a
        // message with the handle method.

        super.setSuccessor(this);
        return this;
    }

    @Override
    public TnccsBatch collect() {
        TnccsBatch b = null;

        if (super.getSuccessor() == null) {

            b = StateUtil.createCloseBatch(this.server);
            super.setSuccessor(this);

        }

        return b;
    }

    @Override
    public TnccsBatch handle(final TnccsBatchContainer batchContainer) {
        TnccsBatch b = null;
        if (super.getSuccessor() == null) {

            NotNull.check("Batch cannot be null. "
                    + "The state transitions seem corrupted.",
                    batchContainer.getResult());

            if (batchContainer.getResult() instanceof PbBatch) {
                PbBatch current = (PbBatch) batchContainer.getResult();

                if (current.getHeader().getType()
                        .equals(PbBatchTypeEnum.CLOSE)) {
                    this.handleClose(batchContainer);
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
                                + ". The state transitions seem corrupted."
                                );

            }
        }

        return b;

    }

    @Override
    public State getConclusiveState() {
        // the end is the end
        return this;
    }

    /**
     * Handles a final message batch and delivers the
     * remaining messages.
     *
     * @param batchContainer the container containing a batch with
     * last messages
     */
    private void handleClose(final TnccsBatchContainer batchContainer) {
        PbBatch b = (PbBatch) batchContainer.getResult();
        if (b.getMessages() != null) {
            this.helper.getHandler().dumpMessages(b.getMessages());
        }

        if (batchContainer.getExceptions() != null) {
            this.helper.getHandler().dumpExceptions(
                    batchContainer.getExceptions());
        }
    }

}
