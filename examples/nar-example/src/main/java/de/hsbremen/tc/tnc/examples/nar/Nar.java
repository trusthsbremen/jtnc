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
package de.hsbremen.tc.tnc.examples.nar;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Properties;

import org.ietf.nea.pb.serialize.reader.bytebuffer.PbReaderFactory;
import org.ietf.nea.pb.serialize.writer.bytebuffer.PbWriterFactory;
import org.ietf.nea.pt.serialize.reader.bytebuffer.PtTlsReaderFactory;
import org.ietf.nea.pt.serialize.writer.bytebuffer.PtTlsWriterFactory;
import org.ietf.nea.pt.socket.simple.DefaultSocketTransportConnectionBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trustedcomputinggroup.tnc.ifimc.IMC;

import de.hsbremen.tc.tnc.message.t.enums.TcgTProtocolBindingEnum;
import de.hsbremen.tc.tnc.tnccs.adapter.im.ImcAdapterFactoryIetf;
import de.hsbremen.tc.tnc.tnccs.adapter.tnccs.TnccAdapterFactoryIetf;
import de.hsbremen.tc.tnc.tnccs.client.ClientFacade;
import de.hsbremen.tc.tnc.tnccs.client.DefaultClientFacade;
import de.hsbremen.tc.tnc.tnccs.client.GlobalHandshakeRetryProxy;
import de.hsbremen.tc.tnc.tnccs.client.enums.CommonConnectionChangeTypeEnum;
import de.hsbremen.tc.tnc.tnccs.im.GlobalHandshakeRetryListener;
import de.hsbremen.tc.tnc.tnccs.im.loader.ConfigurationEntryHandler;
import de.hsbremen.tc.tnc.tnccs.im.loader.ConfigurationFileChangeMonitor;
import de.hsbremen.tc.tnc.tnccs.im.loader.ConfigurationFileParser;
import de.hsbremen.tc.tnc.tnccs.im.loader.simple.DefaultConfigurationFileChangeListener;
import de.hsbremen.tc.tnc.tnccs.im.loader.simple.DefaultConfigurationFileChangeMonitor;
import de.hsbremen.tc.tnc.tnccs.im.loader.simple.DefaultConfigurationFileParserImJava;
import de.hsbremen.tc.tnc.tnccs.im.loader.simple.DefaultImLoader;
import de.hsbremen.tc.tnc.tnccs.im.loader.simple.DefaultImcManagerConfigurationEntryHandler;
import de.hsbremen.tc.tnc.tnccs.im.manager.ImcManager;
import de.hsbremen.tc.tnc.tnccs.im.manager.exception.ImInitializeException;
import de.hsbremen.tc.tnc.tnccs.im.manager.simple.DefaultImcManager;
import de.hsbremen.tc.tnc.tnccs.im.route.DefaultImMessageRouter;
import de.hsbremen.tc.tnc.tnccs.session.base.SessionFactory;
import de.hsbremen.tc.tnc.tnccs.session.base.simple.DefaultClientSessionFactory;
import de.hsbremen.tc.tnc.transport.TransportConnection;
import de.hsbremen.tc.tnc.util.ConfigurationPropertiesLoader;

/**
 * An example Network Access Requestor.
 * Requests handshakes at localhost:10229.
 *
 * The NAR uses a plain socket for testing, this
 * cannot be used for production where TLS
 * protection is mandatory. However to achieve
 * TLS protection just us a Java SSL socket.
 *
 */
public class Nar {

    private static final Logger LOGGER = LoggerFactory.getLogger(Nar.class);
    
    private static final String PROP_NAME_MAX_MSG_SIZE = "max_msg_size";
    private static final String PROP_NAME_MAX_ROUND_TRIP = "max_round_trip";
    private static final String PROP_NAME_SESSION_CLEAN_INTERVAL = "session_clean_interval";
    private static final String PROP_NAME_FILE_CHECK_INTERVAL = "file_check_interval";
    private static final String PROP_NAME_IM_DEFAULT_TIMEOUT = "im_default_timeout";
    
    private static final long DEFAULT_MAX_MSG_SIZE = 131072;
    private static final long DEFAULT_MAX_ROUND_TRIP = 1;
    private static final long DEFAULT_SESSION_CLEAN_INTERVAL = 3000;
    private static final long DEFAULT_FILE_CHECK_INTERVAL = 5000;

    private final long maxMsgSize;
    private final long maxRoundTrip;
    private final long sessionCleanInterval;
    private final long fileCheckInterval;
    private final long imDefaultTimeout;
    
    private ClientFacade client;
    private ImcManager manager;
    private ConfigurationFileChangeMonitor monitor;
    private DefaultSocketTransportConnectionBuilder connectionBuilder;
    private Socket socket;
    private TransportConnection connection;

    /**
     * Creates the NAR using default values.
     */
    public Nar(String propertiesFile) {
        
        Properties properties = null;
        try {
            properties = ConfigurationPropertiesLoader
                .loadProperties(propertiesFile, this.getClass());
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            properties = null;
        }
        
        this.maxMsgSize = (properties != null) ?
                Long.parseLong(properties.getProperty(PROP_NAME_MAX_MSG_SIZE,
                        Long.toString(Nar.DEFAULT_MAX_MSG_SIZE))) :
                            Nar.DEFAULT_MAX_MSG_SIZE;
        this.maxRoundTrip = (properties != null) ?
                Long.parseLong(properties.getProperty(PROP_NAME_MAX_ROUND_TRIP,
                        Long.toString(Nar.DEFAULT_MAX_ROUND_TRIP))) :
                            Nar.DEFAULT_MAX_ROUND_TRIP;
        
        this.sessionCleanInterval = (properties != null) ?
                Long.parseLong(properties.getProperty(PROP_NAME_SESSION_CLEAN_INTERVAL,
                        Long.toString(Nar.DEFAULT_SESSION_CLEAN_INTERVAL))) :
                            Nar.DEFAULT_SESSION_CLEAN_INTERVAL;
        
        this.fileCheckInterval = (properties != null) ?
                Long.parseLong(properties.getProperty(PROP_NAME_FILE_CHECK_INTERVAL,
                        Long.toString(Nar.DEFAULT_FILE_CHECK_INTERVAL))) :
                            Nar.DEFAULT_FILE_CHECK_INTERVAL;        
        
        this.imDefaultTimeout = (properties != null) ?
                Long.parseLong(properties.getProperty(PROP_NAME_IM_DEFAULT_TIMEOUT,
                        Long.toString(ImcAdapterFactoryIetf.DEFAULT_TIMEOUT))) :
                            ImcAdapterFactoryIetf.DEFAULT_TIMEOUT;            

        GlobalHandshakeRetryProxy retryProxy = new GlobalHandshakeRetryProxy();

        this.manager = new DefaultImcManager(new DefaultImMessageRouter(),
                new ImcAdapterFactoryIetf(
                        this.imDefaultTimeout),
                        new TnccAdapterFactoryIetf(retryProxy));

        SessionFactory factory = new DefaultClientSessionFactory(
                PbReaderFactory.getProtocolIdentifier(),
                PbWriterFactory.createExperimentalDefault(),
                PbReaderFactory.createExperimentalDefault(), this.manager);

        this.client = new DefaultClientFacade(factory, this.sessionCleanInterval);

        if (this.client instanceof GlobalHandshakeRetryListener) {
            retryProxy.register((GlobalHandshakeRetryListener) this.client);
        }
        

        final int estimatedDefaultImCount = 10;
        this.connectionBuilder =
                new DefaultSocketTransportConnectionBuilder(
                false,
                TcgTProtocolBindingEnum.PLAIN1,
                PtTlsWriterFactory.createProductionDefault(),
                PtTlsReaderFactory.createProductionDefault())
            .setMessageLength(this.maxMsgSize)
            .setImMessageLength(this.maxMsgSize / estimatedDefaultImCount)
            .setMaxRoundTrips(this.maxRoundTrip);

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
        ConfigurationEntryHandler handler =
                new DefaultImcManagerConfigurationEntryHandler(
                loader, this.manager);

        ConfigurationFileParser parser =
                new DefaultConfigurationFileParserImJava(false);
        DefaultConfigurationFileChangeListener listener =
                new DefaultConfigurationFileChangeListener(parser);
        listener.addHandler(handler.getSupportedConfigurationLines(),
                handler);

        this.monitor = new DefaultConfigurationFileChangeMonitor(
                file, this.fileCheckInterval, true);
        this.monitor.add(listener);
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
     * @param host the address of an NAA
     * @param port the port of an NAA
     * @throws IOException if connection attempt fails
     */
    public void startHandshake(String host, int port) throws IOException {
        if(this.connection != null && this.connection.isOpen()){
            this.client.notifyConnectionChange(this.connection,
                    CommonConnectionChangeTypeEnum.CLOSE);
        }
        this.socket = new Socket(host, port);

        this.connection = this.connectionBuilder.toConnection(true, socket);
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
                CommonConnectionChangeTypeEnum.CLOSE);
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
