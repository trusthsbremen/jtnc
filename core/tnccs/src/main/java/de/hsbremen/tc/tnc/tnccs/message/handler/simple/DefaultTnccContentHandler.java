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
 * @author Carl-Heinz Genzel
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