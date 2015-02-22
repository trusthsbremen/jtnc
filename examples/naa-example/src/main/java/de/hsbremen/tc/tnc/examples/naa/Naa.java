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
package de.hsbremen.tc.tnc.examples.naa;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.ietf.nea.pb.serialize.reader.bytebuffer.PbReaderFactory;
import org.ietf.nea.pb.serialize.writer.bytebuffer.PbWriterFactory;
import org.ietf.nea.pt.serialize.reader.bytebuffer.PtTlsReaderFactory;
import org.ietf.nea.pt.serialize.writer.bytebuffer.PtTlsWriterFactory;
import org.ietf.nea.pt.socket.SocketTransportConnectionBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trustedcomputinggroup.tnc.ifimv.IMV;

import de.hsbremen.tc.tnc.message.t.enums.TcgTProtocolEnum;
import de.hsbremen.tc.tnc.message.t.enums.TcgTVersionEnum;
import de.hsbremen.tc.tnc.tnccs.adapter.im.ImvAdapterFactoryIetf;
import de.hsbremen.tc.tnc.tnccs.adapter.tnccs.TncsAdapterFactoryIetf;
import de.hsbremen.tc.tnc.tnccs.client.ClientFacade;
import de.hsbremen.tc.tnc.tnccs.client.DefaultClientFacade;
import de.hsbremen.tc.tnc.tnccs.client.GlobalHandshakeRetryProxy;
import de.hsbremen.tc.tnc.tnccs.client.enums.CommonConnectionChangeTypeEnum;
import de.hsbremen.tc.tnc.tnccs.im.GlobalHandshakeRetryListener;
import de.hsbremen.tc.tnc.tnccs.im.loader.ConfigurationEntryChangeListener;
import de.hsbremen.tc.tnc.tnccs.im.loader.ConfigurationFileChangeMonitor;
import de.hsbremen.tc.tnc.tnccs.im.loader.DefaultConfigurationMonitorBuilder;
import de.hsbremen.tc.tnc.tnccs.im.loader.simple.DefaultImvLoader;
import de.hsbremen.tc.tnc.tnccs.im.loader.simple
.DefaultImvManagerConfigurationChangeListener;
import de.hsbremen.tc.tnc.tnccs.im.manager.ImvManager;
import de.hsbremen.tc.tnc.tnccs.im.manager.exception.ImInitializeException;
import de.hsbremen.tc.tnc.tnccs.im.manager.simple.DefaultImvManager;
import de.hsbremen.tc.tnc.tnccs.im.route.DefaultImMessageRouter;
import de.hsbremen.tc.tnc.tnccs.session.base.SessionFactory;
import de.hsbremen.tc.tnc.tnccs.session.base.simple.DefaultServerSessionFactory;
import de.hsbremen.tc.tnc.transport.TransportConnection;

/**
 * An example Network Access Authority.
 * Listens for handshakes at localhost:10229.
 *
 * @author Carl-Heinz Genzel
 *
 */
public class Naa {
    private static final Logger LOGGER = LoggerFactory.getLogger(Naa.class);
    private static final long MAX_MSG_SIZE = 131072;
    private static final int NAA_PORT = 10229;
    private static final long SESSION_CLEAN_INTERVAL = 3000;
    private static final long FILE_CHECK_INTERVAL = 5000;

    private ClientFacade client;
    private ImvManager manager;
    private ServerSocket serverSocket;
    private SocketTransportConnectionBuilder connectionBuilder;
    private ConfigurationFileChangeMonitor monitor;
    private ExecutorService runner;
    private boolean stopped;

    /**
     * Creates the NAA using default values.
     */
    public Naa() {

        GlobalHandshakeRetryProxy retryProxy = new GlobalHandshakeRetryProxy();

        this.manager = new DefaultImvManager(new DefaultImMessageRouter(),
                new ImvAdapterFactoryIetf(), new TncsAdapterFactoryIetf(
                        retryProxy));

        this.connectionBuilder = new SocketTransportConnectionBuilder(
                TcgTProtocolEnum.PLAIN.value(), TcgTVersionEnum.V1.value(),
                PtTlsWriterFactory.createProductionDefault(),
                PtTlsReaderFactory.createProductionDefault(MAX_MSG_SIZE));
        /*
         * Just for info, limiting possible but not done here.
         * this.connectionBuilder.setMaxRoundTrips(1);
         */

        this.connectionBuilder.setMessageLength(MAX_MSG_SIZE)
                .setImMessageLength(MAX_MSG_SIZE / 10);

        SessionFactory factory = new DefaultServerSessionFactory(
                PbReaderFactory.getTnccsProtocol(),
                PbReaderFactory.getTnccsVersion(),
                PbWriterFactory.createExperimentalDefault(),
                PbReaderFactory.createExperimentalDefault(), this.manager);

        this.client = new DefaultClientFacade(factory, SESSION_CLEAN_INTERVAL);

        if (this.client instanceof GlobalHandshakeRetryListener) {
            retryProxy.register((GlobalHandshakeRetryListener) this.client);
        }

        this.stopped = true;

        this.runner = Executors.newSingleThreadExecutor();
    }

    /**
     * Creates a file monitor to load IMC using a configuration file.
     *
     * @param file the configuration file
     */
    public void loadImvFromConfigurationFile(final File file) {
        DefaultImvLoader loader = new DefaultImvLoader();
        ConfigurationEntryChangeListener listener =
                new DefaultImvManagerConfigurationChangeListener(
                loader, this.manager);

        this.monitor = new DefaultConfigurationMonitorBuilder(
                FILE_CHECK_INTERVAL, true)
                .addChangeListener(listener).createMonitor(file);
        this.monitor.start();
    }

    /**
     * Imports a list of IMV to the manager and
     * initializes them.
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
        this.runner.execute(new ServerRunner());
    }

    /**
     * Stops the NAA.
     */
    public synchronized void stop() {

        if (!stopped) {

            this.stopped = true;

            this.client.notifyGlobalConnectionChange(
                    CommonConnectionChangeTypeEnum.CLOSED);

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
     * Runnable which manages the server socket to accept connections
     * and dispatch them to the TNCS.
     *
     * @author Carl-Heinz Genzel
     *
     */
    private class ServerRunner implements Runnable {

        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(NAA_PORT);
                while (!Thread.currentThread().isInterrupted()) {
                    LOGGER.info("Listening...");
                    Socket socket = serverSocket.accept();

                    LOGGER.info("Socket accepted " + socket.toString());
                    if (socket != null) {
                        TransportConnection connection = connectionBuilder
                                .toConnection(false, true, socket);
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
