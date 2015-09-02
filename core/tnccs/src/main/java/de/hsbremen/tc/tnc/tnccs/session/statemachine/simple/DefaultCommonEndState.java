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
