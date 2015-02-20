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
import java.util.List;

import org.ietf.nea.pb.batch.PbBatchFactoryIetf;

import de.hsbremen.tc.tnc.connection.DefaultTncConnectionStateEnum;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessage;
import de.hsbremen.tc.tnc.tnccs.message.handler.TnccContentHandler;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.AbstractState;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.Retry;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.State;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.StateHelper;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.enums.TnccsStateEnum;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.simple.util.StateUtil;

/**
 * Default TNCC retry state. The TNCC handles a handshake retry request in the
 * retry state.
 *
 * @author Carl-Heinz Genzel
 *
 */
class DefaultClientRetryState extends AbstractState implements Retry {

    private final StateHelper<TnccContentHandler> helper;

    /**
     * Creates the state with the given state helper.
     *
     * @param helper the state helper
     */
    DefaultClientRetryState(final StateHelper<TnccContentHandler> helper) {

        this.helper = helper;
    }

    @Override
    public TnccsBatch collect() {
        TnccsBatch b = null;

        this.helper.getHandler().setConnectionState(
                DefaultTncConnectionStateEnum.TNC_CONNECTION_STATE_HANDSHAKE);

        List<TnccsMessage> messages = this.helper.getHandler()
                .collectMessages();
        try {

            b = this.createServerBatch(messages);
            super.setSuccessor(this.helper
                    .getState(TnccsStateEnum.SERVER_WORKING));

        } catch (ValidationException e) {

            TnccsMessage error = StateUtil.createLocalError();
            b = StateUtil.createCloseBatch(false, error);
            super.setSuccessor(this.helper.getState(TnccsStateEnum.END));

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
