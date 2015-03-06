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
