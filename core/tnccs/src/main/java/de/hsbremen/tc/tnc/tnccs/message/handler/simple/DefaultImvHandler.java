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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.ietf.nea.pb.message.PbMessageValueIm;
import org.ietf.nea.pb.message.enums.PbMessageImFlagEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.connection.DefaultTncConnectionStateEnum;
import de.hsbremen.tc.tnc.connection.TncConnectionState;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.exception.enums.TncExceptionCodeEnum;
import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessage;
import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessageValue;
import de.hsbremen.tc.tnc.report.ImvRecommendationPair;
import de.hsbremen.tc.tnc.tnccs.adapter.connection.ImvConnectionAdapter;
import de.hsbremen.tc.tnc.tnccs.adapter.connection.ImvConnectionAdapterFactory;
import de.hsbremen.tc.tnc.tnccs.adapter.connection.ImvConnectionContext;
import de.hsbremen.tc.tnc.tnccs.adapter.im.ImvAdapter;
import de.hsbremen.tc.tnc.tnccs.adapter.im.exception.TerminatedException;
import de.hsbremen.tc.tnc.tnccs.im.manager.ImAdapterManager;
import de.hsbremen.tc.tnc.tnccs.im.route.ImMessageRouter;
import de.hsbremen.tc.tnc.tnccs.im.route.exception.NoRecipientFoundException;
import de.hsbremen.tc.tnc.tnccs.message.handler.ImvHandler;

/**
 * Default IMV handler to handle messages destined to/from an IMV.
 *
 *
 */
public class DefaultImvHandler implements ImvHandler {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(DefaultImvHandler.class);

    private Map<Long, ImvAdapter> imAdapters;
    private Map<Long, ImvConnectionAdapter> connections;

    private TncConnectionState state;
    private boolean handshakeBegin;

    private final ImMessageRouter router;
    private ImAdapterManager<ImvAdapter> manager;

    private final ImvConnectionAdapterFactory connectionFactory;
    private final ImvConnectionContext connectionContext;

    /**
     * Creates an IMV handler with the given IMV adapter manager
     * to manage the IMV and the given connection factory to create IMV
     * connections for every IMV with the given IMV connection
     * context. The message router is used to route the message
     * based on its component type address to IMV that are interested
     * in that type.
     *
     * @param manager the IMV manager
     * @param connectionFactory the IMV connection factory
     * @param connectionContext the IMV connection context
     * @param router the message router
     */
    public DefaultImvHandler(final ImAdapterManager<ImvAdapter> manager,
            final ImvConnectionAdapterFactory connectionFactory,
            final ImvConnectionContext connectionContext,
            final ImMessageRouter router) {
        this.connectionFactory = connectionFactory;
        this.manager = manager;
        this.connectionContext = connectionContext;

        Map<Long, ImvAdapter> adapterList = this.manager.getAdapter();
        Map<Long, ImvConnectionAdapter> connectionList = new HashMap<>(
                adapterList.size());

        for (Long key : adapterList.keySet()) {
            connectionList.put(adapterList.get(key).getPrimaryId(),
                    this.connectionFactory.createConnection(adapterList
                            .get(key).getPrimaryId()));
        }

        this.imAdapters = adapterList;
        this.connections = connectionList;
        this.router = router;

        this.state = DefaultTncConnectionStateEnum.HSB_CONNECTION_STATE_UNKNOWN;
        this.handshakeBegin = false;

    }

    @Override
    public void setConnectionState(final TncConnectionState imConnectionState) {

        this.state = imConnectionState;

        if (imConnectionState.equals(DefaultTncConnectionStateEnum
                .TNC_CONNECTION_STATE_HANDSHAKE)) {

            this.handshakeBegin = true;
            this.refreshAdapterEntries();
            this.connectionContext.validate();
        }

        for (Iterator<Entry<Long, ImvAdapter>> iter = this.imAdapters
                .entrySet().iterator(); iter.hasNext();) {
            Entry<Long, ImvAdapter> entry = iter.next();

            try {

                entry.getValue()
                        .notifyConnectionChange(
                                this.connections.get(entry.getKey()),
                                imConnectionState);

            } catch (TerminatedException e) {
                this.connections.remove(entry.getKey());
                iter.remove();

            } catch (TncException e) {
                if (e.getResultCode().equals(
                        TncExceptionCodeEnum.TNC_RESULT_FATAL)) {
                    this.connections.remove(entry.getKey());
                    this.manager.notifyFatalError(entry.getKey(), e);
                    iter.remove();

                }
                LOGGER.error(e.getMessage(), e);
            }

        }

        if (imConnectionState.id()
                == DefaultTncConnectionStateEnum
                .TNC_CONNECTION_STATE_DELETE.id()) {

            this.imAdapters.clear();
            this.connections.clear();
            this.handshakeBegin = false;
            this.connectionContext.invalidate();
        }

    }

    @Override
    public List<TnccsMessage> requestMessages() {
        this.checkState();

        for (Iterator<Entry<Long, ImvAdapter>> iter = this.imAdapters
                .entrySet().iterator(); iter.hasNext();) {
            Entry<Long, ImvAdapter> entry = iter.next();

            try {

                entry.getValue().beginHandshake(
                        this.connections.get(entry.getKey()));

            } catch (UnsupportedOperationException e) {

                LOGGER.debug(
                        "TNCS first support was not identifiable. "
                        + "The feature is not used.",
                        e);

            } catch (TerminatedException e) {
                this.connections.remove(entry.getKey());
                iter.remove();

            } catch (TncException e) {
                if (e.getResultCode().equals(
                        TncExceptionCodeEnum.TNC_RESULT_FATAL)) {
                    this.connections.remove(entry.getKey());
                    this.manager.notifyFatalError(entry.getKey(), e);
                    iter.remove();
                }
                LOGGER.error(e.getMessage(), e);
            }
        }

        if (this.handshakeBegin) {
            this.handshakeBegin = false;
        }

        return this.connectionContext.clearMessage();
    }

    @Override
    public List<TnccsMessage> forwardMessage(final TnccsMessage message) {
        this.checkState();

        if (message == null || message.getValue() == null) {
            LOGGER.debug("Because Message or message value is null,"
                    + " it is ignored.");
            return new ArrayList<TnccsMessage>();
        }

        TnccsMessageValue value = message.getValue();

        if (value instanceof PbMessageValueIm) {

            PbMessageValueIm valueCast = (PbMessageValueIm) value;

            Set<Long> recipients = new HashSet<>();
            if (valueCast.getImFlags().contains(PbMessageImFlagEnum.EXCL)) {
                try {
                    Long recipient = this.router.findExclRecipientId(
                            valueCast.getCollectorId(),
                            valueCast.getSubVendorId(),
                            valueCast.getSubType());
                    recipients.add(recipient);
                } catch (NoRecipientFoundException e) {
                    LOGGER.debug(e.getMessage());
                }

            } else {
                try {
                    recipients = this.router.findRecipientIds(
                        valueCast.getSubVendorId(), valueCast.getSubType());
                } catch (NoRecipientFoundException e) {
                    LOGGER.debug(e.getMessage());
                }
            }

            for (Long recipientId : recipients) {
                if (this.imAdapters.containsKey(recipientId)) {
                    try {

                        this.imAdapters.get(recipientId).handleMessage(
                                this.connections.get(recipientId), value);

                    } catch (TerminatedException e) {
                        this.imAdapters.remove(recipientId);
                        this.connections.remove(recipientId);

                    } catch (TncException e) {
                        if (e.getResultCode().equals(
                                TncExceptionCodeEnum.TNC_RESULT_FATAL)) {
                            this.connections.remove(recipientId);
                            this.manager.notifyFatalError(recipientId, e);
                            this.imAdapters.remove(recipientId);

                        }
                        LOGGER.error(e.getMessage(), e);
                    }
                }
            }

            if (this.handshakeBegin) {
                this.handshakeBegin = false;
            }

        } else {
            LOGGER.debug("Because Message is not of type "
                    + PbMessageValueIm.class.getCanonicalName()
                    + ", it is ignored.");
        }

        return this.connectionContext.clearMessage();
    }

    @Override
    public List<TnccsMessage> lastCall() {

        for (Iterator<Entry<Long, ImvAdapter>> iter = this.imAdapters
                .entrySet().iterator(); iter.hasNext();) {
            Entry<Long, ImvAdapter> entry = iter.next();

            try {

                entry.getValue().batchEnding(
                        this.connections.get(entry.getKey()));

            } catch (TerminatedException e) {
                this.connections.remove(entry.getKey());
                iter.remove();

            } catch (TncException e) {
                if (e.getResultCode().equals(
                        TncExceptionCodeEnum.TNC_RESULT_FATAL)) {
                    this.connections.remove(entry.getKey());
                    this.manager.notifyFatalError(entry.getKey(), e);
                    iter.remove();
                }
                LOGGER.error(e.getMessage(), e);
            }
        }

        return this.connectionContext.clearMessage();
    }

    @Override
    public void dumpMessage(final TnccsMessage message) {
        this.checkState();

        // dumping means, no messages should be send in response
        if (this.connectionContext.isValid()) {
            this.connectionContext.invalidate();
        }

        if (message == null || message.getValue() == null) {
            LOGGER.debug("Because Message or message value is null,"
                    + " it is ignored.");
            return;
        }

        TnccsMessageValue value = message.getValue();

        if (value instanceof PbMessageValueIm) {

            PbMessageValueIm valueCast = (PbMessageValueIm) value;

            Set<Long> recipients = new HashSet<>();
            if (valueCast.getImFlags().contains(PbMessageImFlagEnum.EXCL)) {
                try {
                    Long recipient = this.router.findExclRecipientId(
                        valueCast.getCollectorId(),
                        valueCast.getSubVendorId(),
                        valueCast.getSubType());

                    recipients.add(recipient);
                } catch (NoRecipientFoundException e) {
                    LOGGER.debug(e.getMessage());
                }
            } else {
                try {
                    recipients = this.router.findRecipientIds(
                        valueCast.getSubVendorId(), valueCast.getSubType());
                } catch (NoRecipientFoundException e) {
                    LOGGER.debug(e.getMessage());
                }
            }

            for (Long recipientId : recipients) {

                if (this.imAdapters.containsKey(recipientId)) {
                    try {

                        this.imAdapters.get(recipientId).handleMessage(
                                this.connections.get(recipientId), value);

                    } catch (TerminatedException e) {
                        this.imAdapters.remove(recipientId);
                        this.connections.remove(recipientId);

                    } catch (TncException e) {
                        if (e.getResultCode().equals(
                                TncExceptionCodeEnum.TNC_RESULT_FATAL)) {
                            this.connections.remove(recipientId);
                            this.manager.notifyFatalError(recipientId, e);
                            this.imAdapters.remove(recipientId);
                        }
                        LOGGER.error(e.getMessage(), e);
                    }
                }
            }
        } else {
            LOGGER.debug("Because Message is not of type "
                    + PbMessageValueIm.class.getCanonicalName()
                    + ", it is ignored.");
        }

        // We do not want any messages. Just to be secure discard them all
        this.connectionContext.clearMessage();
    }

    /**
     * Synchronizes the known list of IMC with the manager, which
     * is responsible to manage all IMC.
     */
    private void refreshAdapterEntries() {
        Map<Long, ImvAdapter> list = this.manager.getAdapter();

        Set<Long> oldKeys = new HashSet<>(this.imAdapters.keySet());
        Set<Long> newKeys = new HashSet<>(list.keySet());
        newKeys.removeAll(this.imAdapters.keySet());
        oldKeys.removeAll(list.keySet());

        for (Long long1 : oldKeys) {
            imAdapters.remove(long1);
            connections.remove(long1);
        }

        for (Long long1 : newKeys) {
            ImvAdapter imAdapter = list.get(long1);
            imAdapters.put(imAdapter.getPrimaryId(), imAdapter);
            connections.put(imAdapter.getPrimaryId(), this.connectionFactory
                    .createConnection(imAdapter.getPrimaryId()));
        }
    }

    /**
     * Checks if the current state is a valid connection state.
     */
    private void checkState() {
        if (this.state == null
                || this.state.equals(DefaultTncConnectionStateEnum
                        .HSB_CONNECTION_STATE_UNKNOWN)) {

            throw new IllegalStateException(
                    "The handler's state cannot be:"
                            + ((this.state != null) ? this.state
                                    : DefaultTncConnectionStateEnum
                                        .HSB_CONNECTION_STATE_UNKNOWN)
                                        .toString());
        }
    }

    @Override
    public List<ImvRecommendationPair> solicitRecommendation() {
        this.checkState();

        Map<Long, ImvRecommendationPair> givenRecommendations =
                this.connectionContext.clearRecommendations();

        for (Iterator<Entry<Long, ImvAdapter>> iter = this.imAdapters
                .entrySet().iterator(); iter.hasNext();) {
            Entry<Long, ImvAdapter> entry = iter.next();

            try {
                // dont't ask the IMV's again which already recommended
                // something.
                if (!givenRecommendations.containsKey(entry.getKey())) {
                    entry.getValue().solicitRecommendation(
                            this.connections.get(entry.getKey()));
                }

            } catch (TerminatedException e) {
                this.connections.remove(entry.getKey());
                iter.remove();
            } catch (TncException e) {
                if (e.getResultCode().equals(
                        TncExceptionCodeEnum.TNC_RESULT_FATAL)) {
                    this.connections.remove(entry.getKey());
                    this.manager.notifyFatalError(entry.getKey(), e);
                    iter.remove();
                }
                LOGGER.error(e.getMessage(), e);
            }
        }

        Map<Long, ImvRecommendationPair> missingRecommendations =
                this.connectionContext.clearRecommendations();
        givenRecommendations.putAll(missingRecommendations);

        return new LinkedList<>(givenRecommendations.values());
    }
}
