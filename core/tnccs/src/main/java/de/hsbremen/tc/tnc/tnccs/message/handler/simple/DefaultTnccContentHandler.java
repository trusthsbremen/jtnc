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
package de.hsbremen.tc.tnc.tnccs.message.handler.simple;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.hsbremen.tc.tnc.connection.DefaultTncConnectionStateEnum;
import de.hsbremen.tc.tnc.connection.TncConnectionState;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessage;
import de.hsbremen.tc.tnc.tnccs.message.handler.ImcHandler;
import de.hsbremen.tc.tnc.tnccs.message.handler.TnccContentHandler;
import de.hsbremen.tc.tnc.tnccs.message.handler.TnccHandler;
import de.hsbremen.tc.tnc.tnccs.message.handler.TnccsValidationExceptionHandler;

/**
 * Default TNCC content handler.
 *
 *
 */
public class DefaultTnccContentHandler implements TnccContentHandler {

    private final ImcHandler imHandler;
    private final TnccHandler tnccHandler;
    private final TnccsValidationExceptionHandler exceptionHandler;
    private TncConnectionState connectionState;

    /**
     * Creates a TNCC content handler with the given TNCC message
     * handler, IMC message handler and validation exception handler.
     *
     * @param imHandler the IMC message handler
     * @param tnccHandler the TNCC message handler
     * @param exceptionHandler the validation exception handler
     */
    public DefaultTnccContentHandler(final ImcHandler imHandler,
            final TnccHandler tnccHandler,
            final TnccsValidationExceptionHandler exceptionHandler) {
        this.imHandler = imHandler;
        this.tnccHandler = tnccHandler;
        this.exceptionHandler = exceptionHandler;
        this.connectionState = DefaultTncConnectionStateEnum
                .HSB_CONNECTION_STATE_UNKNOWN;
    }

    @Override
    public void setConnectionState(final TncConnectionState connectionState) {
        this.connectionState = connectionState;
        this.imHandler.setConnectionState(this.connectionState);
        this.tnccHandler.setConnectionState(this.connectionState);
    }

    @Override
    public TncConnectionState getAccessDecision() {
        return this.tnccHandler.getAccessDecision();
    }

    @Override
    public List<TnccsMessage> collectMessages() {
        List<TnccsMessage> messages = new LinkedList<>();

        List<TnccsMessage> temp = this.imHandler.requestMessages();
        if (temp != null) {
            messages.addAll(temp);
        }

        temp = this.tnccHandler.requestMessages();
        if (temp != null) {
            messages.addAll(temp);
        }

        return messages;
    }

    @Override
    public List<TnccsMessage> handleMessages(
            final List<? extends TnccsMessage> list) {
        List<TnccsMessage> messages = new LinkedList<>();
        if (list != null) {
            for (TnccsMessage tnccsMessage : list) {
                /*
                 * TODO make a better filter here, only bring those message to a
                 * handler who can handle it.
                 * e.g. VendorID = IETF_PEN && MessageType = PB_PA
                 * Currently the handler decides which message it accepts.
                 *
                 */
                List<TnccsMessage> temp = this.imHandler
                        .forwardMessage(tnccsMessage);
                if (temp != null) {
                    messages.addAll(temp);
                }

                temp = this.tnccHandler.forwardMessage(tnccsMessage);
                if (temp != null) {
                    messages.addAll(temp);
                }
            }
        }

        List<TnccsMessage> temp = this.imHandler.lastCall();
        if (temp != null) {
            messages.addAll(temp);
        }

        temp = this.tnccHandler.lastCall();
        if (temp != null) {
            messages.addAll(temp);
        }

        return messages;
    }

    @Override
    public List<TnccsMessage> handleExceptions(
            final List<ValidationException> exceptions) {
        List<TnccsMessage> errorMessages = this.exceptionHandler
                .handle(exceptions);
        return (errorMessages != null) ? errorMessages
                : new ArrayList<TnccsMessage>(0);
    }

    @Override
    public void dumpExceptions(final List<ValidationException> exceptions) {
        this.exceptionHandler.dump(exceptions);
    }

    @Override
    public void dumpMessages(final List<? extends TnccsMessage> list) {
        if (list != null) {
            for (TnccsMessage tnccsMessage : list) {
                /*
                 * TODO make a better filter here, only bring those message to a
                 * handler who can handle it.
                 * e.g. VendorID = IETF_PEN && MessageType = PB_PA
                 * Currently the handler decides which message it accepts.
                 */
                this.imHandler.dumpMessage(tnccsMessage);
                this.tnccHandler.dumpMessage(tnccsMessage);
            }
        }
    }

}
