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

import java.util.List;

import org.ietf.nea.pb.batch.PbBatch;

import de.hsbremen.tc.tnc.message.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessage;
import de.hsbremen.tc.tnc.message.tnccs.serialize.TnccsBatchContainer;
import de.hsbremen.tc.tnc.tnccs.message.handler.TnccsContentHandler;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.AbstractState;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.Error;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.State;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.StateHelper;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.enums.TnccsStateEnum;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.simple.util.StateUtil;

/**
 * Default common error state. The TNC(C/S) handles messages
 * or exceptions, that lead to a fatal error by closing the
 * connection with error messages in a final message batch.
 *
 *
 */
class DefaultCommonErrorState extends AbstractState implements Error {

    private final StateHelper<? extends TnccsContentHandler> helper;
    private final boolean server;

    /**
     * Creates the state with the given state helper.
     *
     * @param server true if the state is used within a TNCS
     * @param helper the state helper
     */
    DefaultCommonErrorState(final boolean server,
            final StateHelper<? extends TnccsContentHandler> helper) {

        this.helper = helper;
        this.server = server;
    }

    @Override
    public TnccsBatch handle(final TnccsBatchContainer batchContainer) {

        TnccsBatch b = null;

        if (batchContainer.getResult() == null) {

            if (batchContainer.getExceptions() != null) {

                List<TnccsMessage> messages = this.helper.getHandler()
                        .handleExceptions(batchContainer.getExceptions());
                b = StateUtil.createCloseBatch(server,
                        messages.toArray(new TnccsMessage[messages.size()]));
                super.setSuccessor(this.helper.getState(TnccsStateEnum.END));

            } else {

                b = StateUtil.createCloseBatch(server);
                super.setSuccessor(this.helper.getState(TnccsStateEnum.END));

            }
        } else {

            if (batchContainer.getResult() instanceof PbBatch) {

                TnccsMessage error = StateUtil.createUnexpectedStateError();
                b = StateUtil.createCloseBatch(server, error);
                super.setSuccessor(this.helper.getState(TnccsStateEnum.END));

            } else {

                TnccsMessage error = StateUtil.createUnsupportedVersionError(
                        batchContainer.getResult().getHeader().getVersion(),
                        (short) 2, (short) 2);
                b = StateUtil.createCloseBatch(server, error);
                super.setSuccessor(this.helper.getState(TnccsStateEnum.END));

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
