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
     * Runnable, that removes sessions from the manager,
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
