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
package de.hsbremen.tc.tnc.examples.nar;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

import org.ietf.nea.pb.serialize.reader.bytebuffer.PbReaderFactory;
import org.ietf.nea.pb.serialize.writer.bytebuffer.PbWriterFactory;
import org.ietf.nea.pt.serialize.reader.bytebuffer.PtTlsReaderFactory;
import org.ietf.nea.pt.serialize.writer.bytebuffer.PtTlsWriterFactory;
import org.ietf.nea.pt.socket.SocketTransportConnectionBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trustedcomputinggroup.tnc.ifimc.IMC;

import de.hsbremen.tc.tnc.message.t.enums.TcgTProtocolEnum;
import de.hsbremen.tc.tnc.message.t.enums.TcgTVersionEnum;
import de.hsbremen.tc.tnc.tnccs.adapter.im.ImcAdapterFactoryIetf;
import de.hsbremen.tc.tnc.tnccs.adapter.tnccs.TnccAdapterFactoryIetf;
import de.hsbremen.tc.tnc.tnccs.client.ClientFacade;
import de.hsbremen.tc.tnc.tnccs.client.DefaultClientFacade;
import de.hsbremen.tc.tnc.tnccs.client.GlobalHandshakeRetryProxy;
import de.hsbremen.tc.tnc.tnccs.client.enums.CommonConnectionChangeTypeEnum;
import de.hsbremen.tc.tnc.tnccs.im.GlobalHandshakeRetryListener;
import de.hsbremen.tc.tnc.tnccs.im.loader.ConfigurationEntryChangeListener;
import de.hsbremen.tc.tnc.tnccs.im.loader.ConfigurationFileChangeMonitor;
import de.hsbremen.tc.tnc.tnccs.im.loader.DefaultConfigurationMonitorBuilder;
import de.hsbremen.tc.tnc.tnccs.im.loader.simple.DefaultImLoader;
import de.hsbremen.tc.tnc.tnccs.im.loader.simple
.DefaultImcManagerConfigurationChangeListener;
import de.hsbremen.tc.tnc.tnccs.im.manager.ImcManager;
import de.hsbremen.tc.tnc.tnccs.im.manager.exception.ImInitializeException;
import de.hsbremen.tc.tnc.tnccs.im.manager.simple.DefaultImcManager;
import de.hsbremen.tc.tnc.tnccs.im.route.DefaultImMessageRouter;
import de.hsbremen.tc.tnc.tnccs.session.base.SessionFactory;
import de.hsbremen.tc.tnc.tnccs.session.base.simple.DefaultClientSessionFactory;
import de.hsbremen.tc.tnc.transport.TransportConnection;

/**
 * An example Network Access Requestor. Request handshake at localhost:10229.
 *
 * @author Carl-Heinz Genzel
 *
 */
public class Nar {

    private static final Logger LOGGER = LoggerFactory.getLogger(Nar.class);
    private static final long MAX_MSG_SIZE = 131072;
    private static final int NAA_PORT = 10229;
    private static final long SESSION_CLEAN_INTERVAL = 3000;
    private static final long FILE_CHECK_INTERVAL = 5000;

    private ClientFacade client;
    private ImcManager manager;
    private ConfigurationFileChangeMonitor monitor;
    private Socket socket;
    private TransportConnection connection;

    /**
     * Creates the NAR using default values.
     */
    public Nar() {

        GlobalHandshakeRetryProxy retryProxy = new GlobalHandshakeRetryProxy();

        this.manager = new DefaultImcManager(new DefaultImMessageRouter(),
                new ImcAdapterFactoryIetf(), new TnccAdapterFactoryIetf(
                        retryProxy));

        SessionFactory factory = new DefaultClientSessionFactory(
                PbReaderFactory.getTnccsProtocol(),
                PbReaderFactory.getTnccsVersion(),
                PbWriterFactory.createExperimentalDefault(),
                PbReaderFactory.createExperimentalDefault(), this.manager);

        this.client = new DefaultClientFacade(factory, SESSION_CLEAN_INTERVAL);

        if (this.client instanceof GlobalHandshakeRetryListener) {
            retryProxy.register((GlobalHandshakeRetryListener) this.client);
        }

    }

    /**
     * Starts the NAR.
     */
    public void start() {
        this.client.start();
    }

    /**
     * Creates a file monitor to load IMC using a configuration file.
     *
     * @param file the configuration file
     */
    public void loadImcFromConfigurationFile(final File file) {
        DefaultImLoader<IMC> loader = new DefaultImLoader<IMC>();
        ConfigurationEntryChangeListener listener =
                new DefaultImcManagerConfigurationChangeListener(
                loader, this.manager);

        this.monitor = new DefaultConfigurationMonitorBuilder(
                FILE_CHECK_INTERVAL, true)
                .addChangeListener(listener).createMonitor(file);
        this.monitor.start();
    }

    /**
     * Imports a list of IMC to the manager and initializes them.
     *
     * @param imcs the list of IMC
     */
    public void loadImc(final List<IMC> imcs) {
        // only for testing, IMC/V should be loaded and managed via
        // configuration file.
        for (IMC imc : imcs) {

            try {
                this.manager.add(imc);
            } catch (ImInitializeException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    /**
     * Starts a new integrity handshake.
     *
     * @throws IOException if connection attempt fails
     */
    public void startHandshake() throws IOException {
        this.socket = new Socket("localhost", NAA_PORT);
        SocketTransportConnectionBuilder builder =
                new SocketTransportConnectionBuilder(
                TcgTProtocolEnum.PLAIN.value(), TcgTVersionEnum.V1.value(),
                PtTlsWriterFactory.createProductionDefault(),
                PtTlsReaderFactory.createProductionDefault(MAX_MSG_SIZE));
        /*
         * Just for info, limiting possible but not done here.
         * builder.setMaxRoundTrips(1);
         */

        this.connection = builder.toConnection(true, false, socket);
        this.client.notifyConnectionChange(connection,
                CommonConnectionChangeTypeEnum.NEW);
    }

    /**
     * Stops a handshake.
     *
     * @throws IOException if the connection cannot be closed properly
     */
    public void stopHandshake() throws IOException {
        this.client.notifyConnectionChange(this.connection,
                CommonConnectionChangeTypeEnum.CLOSED);
        this.socket.close();
    }

    /**
     * Stops the NAR.
     */
    public void stop() {
        if (monitor != null) {
            monitor.stop();
        }
        this.client.stop();
        this.manager.terminate();
    }

}
