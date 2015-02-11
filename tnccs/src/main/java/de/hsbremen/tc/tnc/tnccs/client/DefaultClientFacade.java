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
package de.hsbremen.tc.tnc.tnccs.client;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.exception.enums.TncExceptionCodeEnum;
import de.hsbremen.tc.tnc.report.enums.ImHandshakeRetryReasonEnum;
import de.hsbremen.tc.tnc.tnccs.client.enums.ConnectionChangeType;
import de.hsbremen.tc.tnc.tnccs.client.enums.CommonConnectionChangeTypeEnum;
import de.hsbremen.tc.tnc.tnccs.im.GlobalHandshakeRetryListener;
import de.hsbremen.tc.tnc.tnccs.session.base.SessionFactory;
import de.hsbremen.tc.tnc.tnccs.session.base.Session;
import de.hsbremen.tc.tnc.transport.TransportConnection;

/**
 * Default client facade to interact with a TNC(C/S).
 * The facade wraps new connections into TNC(S/C) sessions.
 *
 * @author Carl-Heinz Genzel
 *
 */
public class DefaultClientFacade implements ClientFacade,
        GlobalHandshakeRetryListener {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(DefaultClientFacade.class);

    private static final long DEFAULT_SESSION_CLEANUP_INTERVAL = 5000;

    private final Map<TransportConnection, Session> runningSessions;
    private final SessionFactory sessionFactory;
    private boolean started;
    private final long sessionCleanUpInterval;

    private ExecutorService sessionCleaner;

    /**
     * Creates the default client facade with a given session factory
     * and a clean up interval for session clean up.
     *
     * @param factory the session factory for TNC(C/S)
     * @param sessionCleanUpInterval the clean up interval
     */
    public DefaultClientFacade(final SessionFactory factory,
            final long sessionCleanUpInterval) {

        if (factory != null) {
            this.sessionFactory = factory;
        } else {
            throw new NullPointerException("SessionFactory cannot be null.");
        }

        this.sessionCleanUpInterval = (sessionCleanUpInterval <= 0)
                ? DEFAULT_SESSION_CLEANUP_INTERVAL
                : sessionCleanUpInterval;

        this.runningSessions = new ConcurrentHashMap<>();
        this.started = false;

    }

    @Override
    public void notifyConnectionChange(final TransportConnection connection,
            final ConnectionChangeType change) {

        if (!started) {
            throw new IllegalStateException("Client was not started.");
        }

        if (change.equals(CommonConnectionChangeTypeEnum.CLOSED)) {
            this.closeSession(connection);
        } else if (change.equals(CommonConnectionChangeTypeEnum.NEW)) {
            this.createSession(connection);
        } else {
            LOGGER.warn("Type " + change.toString() + " not implemented.");
        }

    }

    /**
     * Creates a new TNC(C/S) session for the given
     * connection.
     *
     * @param connection the related connection
     */
    private void createSession(final TransportConnection connection) {
        if (this.runningSessions.containsKey(connection)) {
            this.closeSession(connection);
        }

        Session s = this.sessionFactory.createTnccsSession(connection);
        s.start(connection.isSelfInititated());

        this.runningSessions.put(connection, s);

    }

    /**
     * Closes an existing session for the given connection.
     * @param connection the related connection
     */
    private void closeSession(final TransportConnection connection) {
        if (this.runningSessions.containsKey(connection)) {

            if (!this.runningSessions.get(connection).isClosed()) {
                this.runningSessions.get(connection).close();
            }
            this.runningSessions.remove(connection);

        } else {
            LOGGER.debug("No session found for connection "
                    + connection.toString() + ".");
        }
    }

    @Override
    public void notifyGlobalConnectionChange(
            final ConnectionChangeType change) {

        if (!started) {
            throw new IllegalStateException("Client was not started.");
        }

        Set<TransportConnection> keys = new HashSet<>(
                this.runningSessions.keySet());

        for (Iterator<TransportConnection> iter = keys.iterator(); iter
                .hasNext();) {
            TransportConnection connection = iter.next();
            this.notifyConnectionChange(connection, change);
            iter.remove();
        }

        // just to be sure
        if (change.equals(CommonConnectionChangeTypeEnum.CLOSED)) {
            this.runningSessions.clear();
        }

    }

    @Override
    public void start() {
        this.started = true;

        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
        this.sessionCleaner = service;
        service.scheduleWithFixedDelay(
                new Runnable() {

                    @Override
                    public void run() {
                        while (!Thread.currentThread().isInterrupted()) {
                            for (Iterator<Entry<TransportConnection, Session>>
                                iter = runningSessions.entrySet().iterator();
                                iter.hasNext();) {

                                Entry<TransportConnection, Session> e = iter
                                        .next();
                                if (e.getValue().isClosed()
                                        || !e.getKey().isOpen()) {
                                    iter.remove();
                                }

                            }
                            try {
                                Thread.sleep(sessionCleanUpInterval);
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                        }

                    }
                }, this.sessionCleanUpInterval, this.sessionCleanUpInterval,
                TimeUnit.MILLISECONDS);

    }

    @Override
    public void stop() {
        this.sessionCleaner.shutdownNow();

        if (!started) {
            throw new IllegalStateException("Client was not started.");
        }

        this.notifyGlobalConnectionChange(
                CommonConnectionChangeTypeEnum.CLOSED);

    }

    @Override
    public void requestGlobalHandshakeRetry(
            final ImHandshakeRetryReasonEnum reason)
            throws TncException {
        // TODO make this possible?
        throw new TncException("Global handshake retry is not supported.",
                TncExceptionCodeEnum.TNC_RESULT_CANT_RETRY);

    }

}
