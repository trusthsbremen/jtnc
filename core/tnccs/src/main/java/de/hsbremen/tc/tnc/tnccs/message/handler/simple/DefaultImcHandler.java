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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.ietf.nea.pb.message.PbMessageValueIm;
import org.ietf.nea.pb.message.enums.PbMessageImFlagEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.attribute.TncClientAttributeTypeEnum;
import de.hsbremen.tc.tnc.connection.DefaultTncConnectionStateEnum;
import de.hsbremen.tc.tnc.connection.TncConnectionState;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.exception.enums.TncExceptionCodeEnum;
import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessage;
import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessageValue;
import de.hsbremen.tc.tnc.tnccs.adapter.connection.ImcConnectionAdapter;
import de.hsbremen.tc.tnc.tnccs.adapter.connection.ImcConnectionAdapterFactory;
import de.hsbremen.tc.tnc.tnccs.adapter.connection.ImcConnectionContext;
import de.hsbremen.tc.tnc.tnccs.adapter.im.ImcAdapter;
import de.hsbremen.tc.tnc.tnccs.adapter.im.exception.TerminatedException;
import de.hsbremen.tc.tnc.tnccs.im.manager.ImAdapterManager;
import de.hsbremen.tc.tnc.tnccs.im.route.ImMessageRouter;
import de.hsbremen.tc.tnc.tnccs.im.route.exception.NoRecipientFoundException;
import de.hsbremen.tc.tnc.tnccs.message.handler.ImcHandler;

/**
 * Default IMC handler to handle messages destined to/from an IMC.
 *
 * @author Carl-Heinz Genzel
 *
 */
public class DefaultImcHandler implements ImcHandler {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(DefaultImcHandler.class);

    private Map<Long, ImcAdapter> imAdapters;
    private Map<Long, ImcConnectionAdapter> connections;

    private TncConnectionState state;
    private boolean handshakeBegin;

    private final ImMessageRouter router;
    private final ImAdapterManager<ImcAdapter> manager;

    private final ImcConnectionAdapterFactory connectionFactory;
    private final ImcConnectionContext connectionContext;

    /**
     * Creates an IMC handler with the given IMC adapter manager
     * to manage the IMC and the given connection factory to create IMC
     * connections for every IMC with the given IMC connection
     * context. The message router is used to route the message
     * based on its component type address to IMC that are interested
     * in that type.
     *
     * @param manager the IMC manager
     * @param connectionFactory the IMC connection factory
     * @param connectionContext the IMC connection context
     * @param router the message router
     */
    public DefaultImcHandler(final ImAdapterManager<ImcAdapter> manager,
            final ImcConnectionAdapterFactory connectionFactory,
            final ImcConnectionContext connectionContext,
            final ImMessageRouter router) {
        this.connectionFactory = connectionFactory;
        this.manager = manager;
        this.connectionContext = connectionContext;

        Map<Long, ImcAdapter> adapterList = this.manager.getAdapter();
        Map<Long, ImcConnectionAdapter> connectionList = new HashMap<>(
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

        for (Iterator<Entry<Long, ImcAdapter>> iter = this.imAdapters
                .entrySet().iterator(); iter.hasNext();) {
            Entry<Long, ImcAdapter> entry = iter.next();

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

        for (Iterator<Entry<Long, ImcAdapter>> iter = this.imAdapters
                .entrySet().iterator(); iter.hasNext();) {
            Entry<Long, ImcAdapter> entry = iter.next();

            try {

                entry.getValue().beginHandshake(
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
                        valueCast.getCollectorId(), valueCast.getSubVendorId(),
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

                    Boolean tncsFirstSupport = Boolean.FALSE;
                    if (this.handshakeBegin) {

                        try {
                            Object o = this.imAdapters
                                    .get(recipientId)
                                    .getAttribute(TncClientAttributeTypeEnum
                                            .TNC_ATTRIBUTEID_IMC_SPTS_TNCS1);
                            if (o instanceof Boolean) {
                                tncsFirstSupport = (Boolean) o;
                            }
                        } catch (TncException
                                | UnsupportedOperationException e) {

                            LOGGER.debug(
                                    "TNCS first support was not identifiable. "
                                    + "The feature is not used.",
                                    e);
                        }

                    }

                    try {

                        if (this.handshakeBegin && !tncsFirstSupport) {
                            this.imAdapters.get(recipientId).beginHandshake(
                                    this.connections.get(recipientId));
                        } else {
                            this.imAdapters.get(recipientId).handleMessage(
                                    this.connections.get(recipientId), value);
                        }

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

        // we do not want any messages. Just to be secure, discard them all
        this.connectionContext.clearMessage();
    }

    @Override
    public List<TnccsMessage> lastCall() {

        for (Iterator<Entry<Long, ImcAdapter>> iter = this.imAdapters
                .entrySet().iterator(); iter.hasNext();) {
            Entry<Long, ImcAdapter> entry = iter.next();

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

    /**
     * Synchronizes the known list of IMC with the manager, which
     * is responsible to manage all IMC.
     */
    private void refreshAdapterEntries() {
        Map<Long, ImcAdapter> list = this.manager.getAdapter();

        Set<Long> oldKeys = new HashSet<>(this.imAdapters.keySet());
        Set<Long> newKeys = new HashSet<>(list.keySet());
        newKeys.removeAll(this.imAdapters.keySet());
        oldKeys.removeAll(list.keySet());

        for (Long long1 : oldKeys) {
            imAdapters.remove(long1);
            connections.remove(long1);
        }

        for (Long long1 : newKeys) {
            ImcAdapter imAdapter = list.get(long1);
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
                || this.state.equals(
                        DefaultTncConnectionStateEnum
                        .HSB_CONNECTION_STATE_UNKNOWN)) {
            throw new IllegalStateException(
                    "The handlers state cannot be:"
                            + ((this.state != null) ? this.state
                                    : DefaultTncConnectionStateEnum
                                        .HSB_CONNECTION_STATE_UNKNOWN)
                                        .toString());
        }
    }
}
