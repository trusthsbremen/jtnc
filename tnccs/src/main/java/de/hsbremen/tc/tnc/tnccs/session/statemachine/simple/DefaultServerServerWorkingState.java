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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.HSBConstants;
import de.hsbremen.tc.tnc.attribute.TncCommonAttributeTypeEnum;
import de.hsbremen.tc.tnc.attribute.TncHsbAttributeTypeEnum;
import de.hsbremen.tc.tnc.connection.DefaultTncConnectionStateEnum;
import de.hsbremen.tc.tnc.connection.TncConnectionState;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessage;
import de.hsbremen.tc.tnc.message.tnccs.serialize.TnccsBatchContainer;
import de.hsbremen.tc.tnc.tnccs.message.handler.TncsContentHandler;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.AbstractState;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.ClientWorking;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.State;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.StateHelper;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.enums.TnccsStateEnum;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.simple.util.StateUtil;

/**
 * Default TNCS server working state. The TNCS handles messages
 * containing data for an integrity check handshake in the
 * server working state.
 *
 * @author Carl-Heinz Genzel
 *
 */
class DefaultServerServerWorkingState extends AbstractState implements
        ClientWorking {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(DefaultServerServerWorkingState.class);
    private final StateHelper<TncsContentHandler> helper;

    /**
     * Creates the state with the given state helper.
     *
     * @param helper the state helper
     */
    DefaultServerServerWorkingState(
            final StateHelper<TncsContentHandler> helper) {

        this.helper = helper;
    }

    @Override
    public TnccsBatch collect() {
        TnccsBatch b = null;

        List<TnccsMessage> messages = this.helper.getHandler()
                .collectMessages();
        try {

            b = this.createClientBatch(messages);
            super.setSuccessor(this.helper
                    .getState(TnccsStateEnum.CLIENT_WORKING));

        } catch (ValidationException e) {

            TnccsMessage error = StateUtil.createLocalError();
            b = StateUtil.createCloseBatch(true, error);
            super.setSuccessor(this.helper.getState(TnccsStateEnum.END));

        }

        return b;
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

                if (current.getHeader().getType().equals(PbBatchTypeEnum.CDATA)
                        || current.getHeader().getType()
                                .equals(PbBatchTypeEnum.CRETRY)) {

                    boolean roundTripOverrunImminent = this.checkRoundTrips();

                    try {
                        b = this.handleClientData(batchContainer);
                        if (!b.getMessages().isEmpty()
                                && !roundTripOverrunImminent) {
                            super.setSuccessor(this.helper
                                    .getState(TnccsStateEnum.CLIENT_WORKING));
                        } else {
                            // if empty, than there is no more to say, so a
                            // decision can be made
                            b = this.createResult();
                            super.setSuccessor(this.helper
                                    .getState(TnccsStateEnum.DECIDED));
                        }

                    } catch (ValidationException e) {
                        TnccsMessage error = StateUtil.createLocalError();
                        b = StateUtil.createCloseBatch(true, error);
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
        }

        return b;

    }

    /**
     * Checks if round trip overrun is imminent.
     * @return true if the current round trip is the last one
     * allowed for the current session
     */
    private boolean checkRoundTrips() {
        try {
            long maxRoundTrips = 0;
            long currentRoundTrips = 0;

            Object temp1 = this.helper.getAttributes().getAttribute(
                    TncCommonAttributeTypeEnum.TNC_ATTRIBUTEID_MAX_ROUND_TRIPS);
            if (temp1 instanceof Long) {

                maxRoundTrips = ((Long) temp1).longValue();

                if (HSBConstants.TCG_IM_MAX_ROUND_TRIPS_UNKNOWN < maxRoundTrips
                        && maxRoundTrips < HSBConstants
                        .TCG_IM_MAX_ROUND_TRIPS_UNLIMITED) {

                    Object temp2 = this.helper.getAttributes().getAttribute(
                            TncHsbAttributeTypeEnum
                            .HSB_ATTRIBUTEID_CURRENT_ROUND_TRIPS);

                    if (temp2 instanceof Long) {
                        currentRoundTrips = ((Long) temp1).longValue();

                        LOGGER.debug(new StringBuilder()
                            .append("Round trip overrun check ( Max:")
                                .append(maxRoundTrips)
                                .append(", Current:").append(currentRoundTrips)
                                .append(", Overrun imminent:")
                              .append((maxRoundTrips - currentRoundTrips <= 1))
                                .append(").").toString());

                        return (maxRoundTrips - currentRoundTrips <= 1);

                    } else {
                        LOGGER.debug("Current round trip attribute not accessib"
                                + "le. Round trips cannot be evaluated");
                        return false;
                    }
                } else {
                    LOGGER.debug("Round trips not restricted. Round trips "
                            + "cannot be evaluated");
                    return false;
                }
            } else {
                LOGGER.debug("Max round trip attribute not accessible. "
                        + "Round trips cannot be evaluated");
                return false;
            }

        } catch (TncException | UnsupportedOperationException e) {
            LOGGER.debug(
                    "Not all round trip attributes where accessible. "
                    + "Round trips cannot be evaluated",
                    e);
            return false;
        }
    }

    /**
     * Creates a message batch with a TNCS result for the TNCC.
     *
     * @return the message batch with result
     * @throws ValidationException if batch creation fails
     */
    private TnccsBatch createResult() throws ValidationException {

        TnccsBatch b = null;

        List<TnccsMessage> messages = ((TncsContentHandler) this.helper
                .getHandler()).solicitRecommendation();

        b = PbBatchFactoryIetf.createResult((messages != null) ? messages
                : new ArrayList<TnccsMessage>(0));

        TncConnectionState state = this.helper.getHandler()
                .getAccessDecision();
        if (state
                .equals(DefaultTncConnectionStateEnum
                        .TNC_CONNECTION_STATE_ACCESS_ALLOWED)
                || state.equals(DefaultTncConnectionStateEnum
                        .TNC_CONNECTION_STATE_ACCESS_ISOLATED)
                || state.equals(DefaultTncConnectionStateEnum
                        .TNC_CONNECTION_STATE_ACCESS_NONE)) {

            this.helper.getHandler().setConnectionState(state);

        } else {

            throw new IllegalStateException("State " + state.toString()
                    + " does not reflect access decision");

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
     * Handles messages from the TNCC and returns a message batch with
     * server data.
     *
     * @param batchContainer the container containing a batch with
     * the messages from a TNCC
     * @return the message batch with server data
     * @throws ValidationException if batch creation fails
     */
    private TnccsBatch handleClientData(
            final TnccsBatchContainer batchContainer)
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

        TnccsBatch b = this.createClientBatch(response);

        return b;
    }

    /**
     * Creates a message batch of TNCS data for the TNCC.
     *
     * @param messages the messages for the batch
     * @return the message batch
     * @throws ValidationException if batch creation fails
     */
    private TnccsBatch createClientBatch(final List<TnccsMessage> messages)
            throws ValidationException {
        return PbBatchFactoryIetf
                .createServerData((messages != null) ? messages
                        : new ArrayList<TnccsMessage>(0));
    }
}
