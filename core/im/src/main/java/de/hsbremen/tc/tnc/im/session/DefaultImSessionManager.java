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

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.connection.DefaultTncConnectionStateEnum;

/**
 * Default session manager. It holds all sessions and manages them according to
 * their state.
 *
 *
 * @param <K> the connection type (e.g. IMC or IMV connection)
 * @param <V> the session type (e.g. IMC or IMV session)
 */
public class DefaultImSessionManager<K, V extends ImSession> implements
        ImSessionManager<K, V> {

    public static final long DEFAULT_CLEANUP_INTERVAL = 3000;

    private static final Logger LOGGER = LoggerFactory
            .getLogger(DefaultImSessionManager.class);
    private final Map<K, V> runningSessions;
    private final long sessionCleanUpInterval;
    private ExecutorService service;

    /**
     * Creates a session manager with a default clean up interval (3 sec.) for
     * session cleanup.
     */
    public DefaultImSessionManager() {
        this(DEFAULT_CLEANUP_INTERVAL);
    }

    /**
     * Creates a session manager with the given clean up interval.
     *
     * @param sessionCleanUpInterval the clean up interval
     */
    public DefaultImSessionManager(final long sessionCleanUpInterval) {
        this.runningSessions = new ConcurrentHashMap<>();
        this.sessionCleanUpInterval = sessionCleanUpInterval;
    }

    @Override
    public V getSession(final K connection) {
        return this.runningSessions.get(connection);
    }

    @Override
    public V putSession(final K connection, final V session) {
        LOGGER.debug("New session " + session.toString() + " for connection "
                + connection.toString() + " added.");
        return this.runningSessions.put(connection, session);
    }

    @Override
    public void initialize() {

        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
        this.service = service;
        service.scheduleWithFixedDelay(new Cleaner(),
                this.sessionCleanUpInterval,
                this.sessionCleanUpInterval,
                TimeUnit.MILLISECONDS);

    }

    @Override
    public void terminate() {
        this.service.shutdownNow();

        for (V session : this.runningSessions.values()) {
            session.terminate();
        }
        this.runningSessions.clear();
    }

    /**
     * Runnable that removes sessions from the manager,
     * which are closed or where the state is unknown.
     *
         *
     */
    private class Cleaner implements Runnable {

        @Override
        public void run() {
            for (Iterator<Entry<K, V>> iter = runningSessions.entrySet()
                    .iterator(); iter.hasNext();) {
                Entry<K, V> e = iter.next();
                if (e.getValue()
                        .getConnectionState()
                        .equals(DefaultTncConnectionStateEnum
                                .HSB_CONNECTION_STATE_UNKNOWN)
                        || e.getValue()
                                .getConnectionState()
                                .equals(DefaultTncConnectionStateEnum
                                        .TNC_CONNECTION_STATE_DELETE)) {

                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug(new StringBuilder()
                                .append("Session entry ")
                                .append(e.getKey().toString())
                                .append(" with state: ")
                                .append(e.getValue().getConnectionState()
                                        .toString())
                                .append(" will be removed.").toString());
                    }
                    iter.remove();
                }
            }
        }
    }
}
