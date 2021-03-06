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
package de.hsbremen.tc.tnc.examples.naa;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.ietf.nea.pb.serialize.reader.bytebuffer.PbReaderFactory;
import org.ietf.nea.pb.serialize.writer.bytebuffer.PbWriterFactory;
import org.ietf.nea.pt.serialize.reader.bytebuffer.PtTlsReaderFactory;
import org.ietf.nea.pt.serialize.writer.bytebuffer.PtTlsWriterFactory;
import org.ietf.nea.pt.socket.simple.DefaultSocketTransportConnectionBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trustedcomputinggroup.tnc.ifimv.IMV;

import de.hsbremen.tc.tnc.message.t.enums.TcgTProtocolBindingEnum;
import de.hsbremen.tc.tnc.tnccs.adapter.im.ImvAdapterFactoryIetf;
import de.hsbremen.tc.tnc.tnccs.adapter.tnccs.TncsAdapterFactoryIetf;
import de.hsbremen.tc.tnc.tnccs.client.ClientFacade;
import de.hsbremen.tc.tnc.tnccs.client.DefaultClientFacade;
import de.hsbremen.tc.tnc.tnccs.client.GlobalHandshakeRetryProxy;
import de.hsbremen.tc.tnc.tnccs.client.enums.CommonConnectionChangeTypeEnum;
import de.hsbremen.tc.tnc.tnccs.im.GlobalHandshakeRetryListener;
import de.hsbremen.tc.tnc.tnccs.im.loader.ConfigurationFileChangeMonitor;
import de.hsbremen.tc.tnc.tnccs.im.loader.ConfigurationFileParser;
import de.hsbremen.tc.tnc.tnccs.im.loader.simple.DefaultConfigurationFileChangeListener;
import de.hsbremen.tc.tnc.tnccs.im.loader.simple.DefaultConfigurationFileChangeMonitor;
import de.hsbremen.tc.tnc.tnccs.im.loader.simple.DefaultConfigurationFileParserImJava;
import de.hsbremen.tc.tnc.tnccs.im.loader.simple.DefaultImLoader;
import de.hsbremen.tc.tnc.tnccs.im.loader.simple.DefaultImvManagerConfigurationEntryHandler;
import de.hsbremen.tc.tnc.tnccs.im.manager.ImvManager;
import de.hsbremen.tc.tnc.tnccs.im.manager.exception.ImInitializeException;
import de.hsbremen.tc.tnc.tnccs.im.manager.simple.DefaultImvManager;
import de.hsbremen.tc.tnc.tnccs.im.route.DefaultImMessageRouter;
import de.hsbremen.tc.tnc.tnccs.session.base.SessionFactory;
import de.hsbremen.tc.tnc.tnccs.session.base.simple.DefaultServerSessionFactory;
import de.hsbremen.tc.tnc.transport.TransportConnection;

/**
 * An example Network Access Authority. Listens for handshakes at
 * localhost:30271.
 *
 * The NAA uses a plain socket for testing, this cannot be used for production
 * where TLS protection is mandatory. However to achieve TLS protection just us
 * a Java SSL socket.
 *
 */
public class Naa {
    private static final Logger LOGGER = LoggerFactory.getLogger(Naa.class);

    static final String PROP_NAME_MAX_MSG_SIZE = "max_msg_size";
    static final String PROP_NAME_MAX_ROUND_TRIP = "max_round_trip";
    static final String PROP_NAME_NAA_PORT = "naa_port";
    static final String PROP_NAME_SESSION_CLEAN_INTERVAL = "session_clean_interval";
    static final String PROP_NAME_FILE_CHECK_INTERVAL = "file_check_interval";
    static final String PROP_NAME_IM_DEFAULT_TIMEOUT = "im_default_timeout";

    static final String DEFAULT_MAX_MSG_SIZE = "131072";
    static final String DEFAULT_MAX_ROUND_TRIP = "1";
    static final String DEFAULT_NAA_PORT = "30271";
    static final String DEFAULT_SESSION_CLEAN_INTERVAL = "3000";
    static final String DEFAULT_FILE_CHECK_INTERVAL = "5000";
    
    private final long maxMsgSize;
    private final long maxRoundTrip;
    private final int naaPort;
    private final long sessionCleanInterval;
    private final long fileCheckInterval;
    private final long imDefaultTimeout;

    private ClientFacade client;
    private ImvManager manager;
    private ServerSocket serverSocket;
    private DefaultSocketTransportConnectionBuilder connectionBuilder;
    private ConfigurationFileChangeMonitor monitor;
    private ExecutorService runner;
    private boolean stopped;

    /**
     * Creates the NAA using default values.
     */
    public Naa(Properties properties) {

        this.maxMsgSize = Long.parseLong((properties != null) ? properties
                .getProperty(PROP_NAME_MAX_MSG_SIZE, DEFAULT_MAX_MSG_SIZE)
                : Naa.DEFAULT_MAX_MSG_SIZE);
        this.maxRoundTrip = Long.parseLong((properties != null) ? properties
                .getProperty(PROP_NAME_MAX_ROUND_TRIP, DEFAULT_MAX_ROUND_TRIP)
                : Naa.DEFAULT_MAX_ROUND_TRIP);

        this.naaPort = Integer.parseInt((properties != null) ? properties
                .getProperty(PROP_NAME_NAA_PORT,DEFAULT_NAA_PORT)
                : Naa.DEFAULT_NAA_PORT);

        this.sessionCleanInterval = Long.parseLong((properties != null) ? properties
                .getProperty(PROP_NAME_SESSION_CLEAN_INTERVAL, DEFAULT_SESSION_CLEAN_INTERVAL)
                : Naa.DEFAULT_SESSION_CLEAN_INTERVAL);

        this.fileCheckInterval = Long.parseLong((properties != null) ? properties
                .getProperty(PROP_NAME_FILE_CHECK_INTERVAL, DEFAULT_FILE_CHECK_INTERVAL)
                : Naa.DEFAULT_FILE_CHECK_INTERVAL);

        this.imDefaultTimeout = Long.parseLong((properties != null) ? properties
                .getProperty(PROP_NAME_IM_DEFAULT_TIMEOUT,
                        Long.toString(ImvAdapterFactoryIetf.DEFAULT_TIMEOUT))
                : Long.toString(ImvAdapterFactoryIetf.DEFAULT_TIMEOUT));


        GlobalHandshakeRetryProxy retryProxy = new GlobalHandshakeRetryProxy();

        this.manager = new DefaultImvManager(new DefaultImMessageRouter(),
                new ImvAdapterFactoryIetf(this.imDefaultTimeout),
                new TncsAdapterFactoryIetf(retryProxy));

        final int estimatedDefaultImCount = 10;
        this.connectionBuilder = new DefaultSocketTransportConnectionBuilder(
                true, TcgTProtocolBindingEnum.PLAIN1,
                PtTlsWriterFactory.createProductionDefault(),
                PtTlsReaderFactory.createProductionDefault())
                .setMessageLength(this.maxMsgSize)
                .setImMessageLength(this.maxMsgSize / estimatedDefaultImCount)
                .setMaxRoundTrips(this.maxRoundTrip);

        SessionFactory factory = new DefaultServerSessionFactory(
                PbReaderFactory.getProtocolIdentifier(),
                PbWriterFactory.createExperimentalDefault(),
                PbReaderFactory.createExperimentalDefault(), this.manager);

        this.client = new DefaultClientFacade(factory,
                this.sessionCleanInterval);

        if (this.client instanceof GlobalHandshakeRetryListener) {
            retryProxy.register((GlobalHandshakeRetryListener) this.client);
        }

        this.stopped = true;

        this.runner = Executors.newSingleThreadExecutor();
    }

    /**
     * Creates a file monitor to load IMV using a configuration file.
     *
     * @param file the configuration file
     */
    public void loadImvFromConfigurationFile(final File file) {
        DefaultImLoader<IMV> loader = new DefaultImLoader<IMV>();
        DefaultImvManagerConfigurationEntryHandler handler = new DefaultImvManagerConfigurationEntryHandler(
                loader, this.manager);

        ConfigurationFileParser parser = new DefaultConfigurationFileParserImJava(
                true);
        DefaultConfigurationFileChangeListener listener = new DefaultConfigurationFileChangeListener(
                parser);
        listener.addHandler(handler.getSupportedConfigurationLines(), handler);

        this.monitor = new DefaultConfigurationFileChangeMonitor(file,
                this.fileCheckInterval, true);
        this.monitor.add(listener);

        this.monitor.start();
    }

    /**
     * Imports a list of IMV to the manager and initializes them.
     * 
     * @param imvs the list of IMV
     */
    public void loadImv(final List<IMV> imvs) {
        // only for testing, later IMC/V are loaded and managed via
        // configuration file.
        for (IMV imv : imvs) {

            try {
                this.manager.add(imv);
            } catch (ImInitializeException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    /**
     * Starts the NAA.
     */
    public void start() {
        this.client.start();
        this.stopped = false;
        this.runner.execute(new ServerRunner(this.naaPort));
    }

    /**
     * Stops the NAA.
     */
    public synchronized void stop() {

        if (!stopped) {

            this.stopped = true;

            this.client
                    .notifyGlobalConnectionChange(CommonConnectionChangeTypeEnum.CLOSE);

            this.runner.shutdownNow();

            try {
                this.serverSocket.close();
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }

            this.monitor.stop();
            this.client.stop();
            this.manager.terminate();

        }
    }

    /**
     * Runnable which manages the server socket to accept connections and
     * dispatch them to the TNCS.
     *
     */
    private class ServerRunner implements Runnable {

        private final int naaPort;

        public ServerRunner(final int naaPort) {
            this.naaPort = naaPort;
        }

        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(this.naaPort);
                while (!Thread.currentThread().isInterrupted()) {
                    LOGGER.info("Listening...");
                    Socket socket = serverSocket.accept();

                    LOGGER.info("Socket accepted " + socket.toString());
                    if (socket != null) {
                        TransportConnection connection = connectionBuilder
                                .toConnection(false, socket);
                        client.notifyConnectionChange(connection,
                                CommonConnectionChangeTypeEnum.NEW);

                    }
                }
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
                stop();
            }

        }

    }
}
