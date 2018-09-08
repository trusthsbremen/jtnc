package de.hsbremen.tc.tnc.im.session;
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


import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.HSBConstants;
import de.hsbremen.tc.tnc.attribute.TncAttributeType;
import de.hsbremen.tc.tnc.connection.DefaultTncConnectionStateEnum;
import de.hsbremen.tc.tnc.connection.TncConnectionState;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.im.adapter.connection.ImConnectionAdapter;
import de.hsbremen.tc.tnc.im.adapter.data.ImObjectComponent;
import de.hsbremen.tc.tnc.im.evaluate.ImEvaluatorManager;
import de.hsbremen.tc.tnc.im.session.enums.ImMessageTriggerEnum;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.report.enums.HandshakeRetryReasonEnum;

/**
 * Generic base for an IM(C/V) session. Especially important for inheritance.
 *
 *
 * @param <T> the related connection type
 */
abstract class AbstractDefaultImSession<T extends ImConnectionAdapter>
        implements ImSession, ImSessionContext {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(AbstractDefaultImSession.class);

    private long messageIndex;

    private TncConnectionState connectionState;

    private final T connection;

    private final ImEvaluatorManager evaluatorManager;

    private EnumSet<HandshakeRetryReasonEnum>
        connectionHandshakeRetryRequested;

    /**
     * Creates the base for an IM(C/V) session.
     *
     * @param connection the connection for the session
     * @param connectionState the initial connection state
     * @param evaluatorManager the evaluation composition for the session
     */
    protected AbstractDefaultImSession(final T connection,
            final TncConnectionState connectionState,
            final ImEvaluatorManager evaluatorManager) {
        this.evaluatorManager = evaluatorManager;
        this.connectionState = connectionState;
        this.connection = connection;
        this.connectionHandshakeRetryRequested = EnumSet
                .noneOf(HandshakeRetryReasonEnum.class);
    }

    @Override
    public final void requestConnectionHandshakeRetry(
            final HandshakeRetryReasonEnum reason) {
        this.connectionHandshakeRetryRequested.add(reason);
    }

    @Override
    public final Object getAttribute(final TncAttributeType type)
            throws TncException {
        return this.connection.getAttribute(type);
    }

    @Override
    public final TncConnectionState getConnectionState() {
        return connectionState;
    }

    @Override
    public final void setConnectionState(
            final TncConnectionState connectionState) {
        LOGGER.debug("Connection state has changed to: "
                + connectionState.toString());
        this.connectionState = connectionState;
    }

    @Override
    public void triggerMessage(final ImMessageTriggerEnum reason)
            throws TncException {
        LOGGER.info("Message trigger activated  for " + reason.toString());

        List<ImObjectComponent> cmpList = new LinkedList<>();

        List<ImObjectComponent> components = null;

        switch (reason) {
        case BEGIN_HANDSHAKE:
            components = evaluatorManager.evaluate(this);
            break;
        case BATCH_ENDING:
            components = evaluatorManager.lastCall(this);
            break;
        default:
            LOGGER.warn("Message trigger reason " + reason + " is unknown.");
        }

        if (components != null && !components.isEmpty()) {
            cmpList.addAll(components);
        }

        this.dispatchMessagesToConnection(cmpList);

    }

    @Override
    public void handleMessage(final ImObjectComponent component)
            throws TncException {
        LOGGER.debug("Handle message with vendor ID " + component.getVendorId()
                + " and type " + component.getType() + ".");

        List<ImObjectComponent> cmpList = new LinkedList<>();

        List<ImObjectComponent> parameterList =
                new ArrayList<ImObjectComponent>();
        parameterList.add(component);

        List<ImObjectComponent> components = this.evaluatorManager.handle(
                parameterList, this);

        if (components != null && !components.isEmpty()) {
            cmpList.addAll(components);
        }

        this.dispatchMessagesToConnection(cmpList);
    }

    @Override
    public void terminate() {
        LOGGER.debug("Terminate called.");
        this.connectionState =
                DefaultTncConnectionStateEnum.TNC_CONNECTION_STATE_DELETE;
    }

    /**
     * Returns a unique message identifier. This is only safe to use until the
     * identifier reaches a value >= 4294967295. After reaching this limit, the
     * identifier is set to zero and starts again, which leads to duplicate
     * identifiers.
     *
     * @return a message identifier
     */
    private long getNextMessageIdentifier() {
        if (this.messageIndex < HSBConstants.TCG_MAX_MESSAGE_IDENTIFIER) {
            return messageIndex++;
        } else {
            this.messageIndex = 0;
            return this.messageIndex;
        }
    }

    /**
     * Sends a integrity measurement component message from the IM(C/V) to the
     * lower layers.
     *
     * @param componentList the list of component messages
     */
    protected void dispatchMessagesToConnection(
            final List<ImObjectComponent> componentList) {
        LOGGER.debug("Dispatch messages to connection "
                + this.connection.toString() + ". Message count: "
                + ((componentList != null) ? componentList.size() : 0));
        if (this.connectionHandshakeRetryRequested.size() > 0) {
            // use the first reason, because only one can be used. It is anyway
            // usually only after remediation.
            try {
                this.connection
                        .requestHandshakeRetry(
                                this.connectionHandshakeRetryRequested
                                .iterator().next());
            } catch (TncException e) {
                LOGGER.warn("Handshake request for connection "
                        + this.connection.toString() + " was not executed.", e);
                for (ImObjectComponent imComponent : componentList) {
                    try {
                        this.connection.sendMessage(imComponent,
                                this.getNextMessageIdentifier());
                    } catch (ValidationException e1) {
                        LOGGER.error(new StringBuilder()
                                .append("Message with ")
                                .append(imComponent.getVendorId())
                                .append(" and type ")
                                .append(imComponent.getType())
                                .append(" could not be send, ")
                                .append("because it contains ")
                                .append("faulty values. \n ")
                                .toString(),
                                e1);
                    } catch (TncException e1) {
                        LOGGER.error(new StringBuilder()
                        .append("Message with ")
                        .append(imComponent.getVendorId())
                        .append(" and type ")
                        .append(imComponent.getType())
                        .append(" could not be send, ")
                        .append("because the connection ")
                        .append("refused it. \n ")
                        .toString(),
                        e1);
                    }
                }

            } finally {
                this.connectionHandshakeRetryRequested.clear();
            }
        } else {
            for (ImObjectComponent imComponent : componentList) {
                try {
                    this.connection.sendMessage(imComponent,
                            this.getNextMessageIdentifier());
                } catch (ValidationException e1) {
                    LOGGER.error(new StringBuilder()
                            .append("Message with ")
                            .append(imComponent.getVendorId())
                            .append(" and type ")
                            .append(imComponent.getType())
                            .append(" could not be send, ")
                            .append("because it contains ")
                            .append("faulty values. \n ")
                            .toString(),
                            e1);
                } catch (TncException e1) {
                    LOGGER.error(new StringBuilder()
                    .append("Message with ")
                    .append(imComponent.getVendorId())
                    .append(" and type ")
                    .append(imComponent.getType())
                    .append(" could not be send, ")
                    .append("because the connection ")
                    .append("refused it. \n ")
                    .toString(),
                    e1);
                }
            }
        }
    }

    /**
     * Returns the evaluator manager.
     * @return the evaluator manager
     */
    protected final ImEvaluatorManager getEvaluator() {
        return this.evaluatorManager;
    }

    /**
     * Returns the underlying connection.
     * @return the underlying connection
     */
    protected final T getConnection() {
        return this.connection;
    }

}
