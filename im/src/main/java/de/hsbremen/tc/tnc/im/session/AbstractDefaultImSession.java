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
package de.hsbremen.tc.tnc.im.session;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.HSBConstants;
import de.hsbremen.tc.tnc.attribute.TncAttributeType;
import de.hsbremen.tc.tnc.connection.TncConnectionState;
import de.hsbremen.tc.tnc.connection.DefaultTncConnectionStateEnum;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.im.adapter.connection.ImConnectionAdapter;
import de.hsbremen.tc.tnc.im.adapter.data.ImObjectComponent;
import de.hsbremen.tc.tnc.im.evaluate.ImEvaluatorManager;
import de.hsbremen.tc.tnc.im.session.enums.ImMessageTriggerEnum;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.report.enums.ImHandshakeRetryReasonEnum;

/**
 * Generic base for an IM(C/V) session.
 *
 * @author Carl-Heinz Genzel
 *
 * @param <T> the related connection type
 */
public abstract class AbstractDefaultImSession<T extends ImConnectionAdapter>
        implements ImSession, ImSessionContext {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(ImSession.class);

    private long messageIndex;

    private TncConnectionState connectionState;

    private final T connection;

    private final ImEvaluatorManager evaluatorManager;

    private EnumSet<ImHandshakeRetryReasonEnum>
        connectionHandshakeRetryRequested;

    /**
     * Creates the base for an IM(C/V) session.
     *
     * @param connection the connection for the session
     * @param connectionState the initial connection state
     * @param evaluatorManager the evaluation composition for the session
     */
    AbstractDefaultImSession(final T connection,
            final TncConnectionState connectionState,
            final ImEvaluatorManager evaluatorManager) {
        this.evaluatorManager = evaluatorManager;
        this.connectionState = connectionState;
        this.connection = connection;
        this.connectionHandshakeRetryRequested = EnumSet
                .noneOf(ImHandshakeRetryReasonEnum.class);
    }

    @Override
    public final void requestConnectionHandshakeRetry(
            final ImHandshakeRetryReasonEnum reason) {
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
     * @throws TncException if the transmission fails
     */
    private void dispatchMessagesToConnection(
            final List<ImObjectComponent> componentList) throws TncException {
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
