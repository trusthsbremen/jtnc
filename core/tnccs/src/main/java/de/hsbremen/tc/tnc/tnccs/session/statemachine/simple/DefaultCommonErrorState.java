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
 * or exceptions that lead to a fatal error by closing the
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
