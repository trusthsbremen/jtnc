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
package de.hsbremen.tc.tnc.im.adapter.imv;

import java.util.HashSet;

import org.ietf.nea.pa.serialize.reader.bytebuffer.PaReaderFactory;
import org.ietf.nea.pa.serialize.writer.bytebuffer.PaWriterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trustedcomputinggroup.tnc.ifimv.IMV;
import org.trustedcomputinggroup.tnc.ifimv.IMVConnection;
import org.trustedcomputinggroup.tnc.ifimv.TNCException;
import org.trustedcomputinggroup.tnc.ifimv.TNCS;

import de.hsbremen.tc.tnc.HSBConstants;
import de.hsbremen.tc.tnc.attribute.TncCommonAttributeTypeEnum;
import de.hsbremen.tc.tnc.connection.DefaultTncConnectionStateFactory;
import de.hsbremen.tc.tnc.connection.TncConnectionState;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.im.adapter.AbstractImAdapter;
import de.hsbremen.tc.tnc.im.adapter.ImParameter;
import de.hsbremen.tc.tnc.im.adapter.connection.ImvConnectionAdapterFactory;
import de.hsbremen.tc.tnc.im.adapter.connection.ImvConnectionAdapterFactoryIetf;
import de.hsbremen.tc.tnc.im.adapter.data.ImComponentFactory;
import de.hsbremen.tc.tnc.im.adapter.data.ImObjectComponent;
import de.hsbremen.tc.tnc.im.adapter.tnccs.TncsAdapter;
import de.hsbremen.tc.tnc.im.adapter.tnccs.TncsAdapterFactory;
import de.hsbremen.tc.tnc.im.adapter.tnccs.TncsAdapterFactoryIetf;
import de.hsbremen.tc.tnc.im.evaluate.ImEvaluatorFactory;
import de.hsbremen.tc.tnc.im.evaluate.ImEvaluatorManager;
import de.hsbremen.tc.tnc.im.evaluate.simple.DefaultImvEvaluatorFactory;
import de.hsbremen.tc.tnc.im.session.DefaultImvSessionFactory;
import de.hsbremen.tc.tnc.im.session.ImSessionFactory;
import de.hsbremen.tc.tnc.im.session.ImvSession;
import de.hsbremen.tc.tnc.im.session.DefaultImSessionManager;
import de.hsbremen.tc.tnc.im.session.ImSessionManager;
import de.hsbremen.tc.tnc.im.session.enums.ImMessageTriggerEnum;
import de.hsbremen.tc.tnc.message.m.serialize.ImMessageContainer;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImReader;
import de.hsbremen.tc.tnc.report.SupportedMessageType;

/**
 * IMV adapter according to IETF/TCG specifications.
 * Implementing a simple IF-IMV IMV interface.
 *
 *
 */
public class ImvAdapterIetf extends AbstractImAdapter implements IMV {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(ImvAdapterIetf.class);

    private final ImParameter parameter;

    private final TncsAdapterFactory tncsFactory;
    private final ImvConnectionAdapterFactory connectionFactory;
    private final ImSessionFactory<ImvSession> sessionFactory;
    private final ImSessionManager<IMVConnection, ImvSession> sessions;

    private final ImEvaluatorFactory evaluatorFactory;
    private ImEvaluatorManager evaluatorManager;

    private TncsAdapter tncs;

    /**
     * Creates an IMV adapter with default arguments. This constructor
     * is specified to be called to load an IMV from a source.
     *
     * A specific IMV must override this constructor and add its custom
     * arguments. This implementation of the constructor should only be
     * used for testing purpose.
     */
    public ImvAdapterIetf() {
        this(new ImParameter(), new TncsAdapterFactoryIetf(),
                new DefaultImvSessionFactory(),
                new DefaultImSessionManager<IMVConnection, ImvSession>(),
                new DefaultImvEvaluatorFactory(),
                new ImvConnectionAdapterFactoryIetf(
                        PaWriterFactory.createProductionDefault()),
                PaReaderFactory.createProductionDefault());
    }

    /**
     * Creates an IMV adapter with the specified arguments. This constructor
     * is especially for inheritance.
     *
     * @param parameter the generic IMV parameter
     * @param tncsFactory the TNCS adapter factory
     * @param sessionFactory the factory for a connection session
     * @param sessionManager the session manager
     * @param evaluatorFactory the factory to instantiate the evaluation system
     * @param connectionFactory the connection adapter factory
     * @param imReader the integrity message reader
     */
    public ImvAdapterIetf(final ImParameter parameter,
            final TncsAdapterFactory tncsFactory,
            final ImSessionFactory<ImvSession> sessionFactory,
            final ImSessionManager<IMVConnection, ImvSession> sessionManager,
            final ImEvaluatorFactory evaluatorFactory,
            final ImvConnectionAdapterFactory connectionFactory,
            final ImReader<? extends ImMessageContainer> imReader) {
        super(imReader);

        this.parameter = parameter;

        this.tncsFactory = tncsFactory;
        this.connectionFactory = connectionFactory;
        this.evaluatorFactory = evaluatorFactory;
        this.sessionFactory = sessionFactory;

        this.sessions = sessionManager;
    }

    @Override
    public void initialize(final TNCS tncs) throws TNCException {
        if (this.tncs == null) {

            this.tncs = this.tncsFactory.createTncsAdapter(this, tncs);
            this.evaluatorManager = this.evaluatorFactory.getEvaluators(
                    this.tncs, this.parameter);

            try {

                this.tncs.reportMessageTypes(this.evaluatorManager
                        .getSupportedMessageTypes());

            } catch (TncException e) {
                throw new TNCException(e.getMessage(), e.getResultCode().id());
            }

            try {

                Object o = tncs.getAttribute(
                        TncCommonAttributeTypeEnum
                        .TNC_ATTRIBUTEID_PREFERRED_LANGUAGE.id());

                if (o instanceof String) {
                    String preferredLanguage = (String) o;
                    this.parameter.setPreferredLanguage(preferredLanguage);
                }

            } catch (TNCException | UnsupportedOperationException e) {
                StringBuilder sb = new StringBuilder();
                sb.append("Preferred language attribute was not accessible. ")
                .append(e.getMessage())
                .append(" Using default language: ")
                .append(this.parameter.getPreferredLanguage())
                .append(".");
                LOGGER.info(sb.toString());
            }

            this.sessions.initialize();

        } else {
            throw new TNCException("IMV already initialized by "
                    + this.tncs.toString() + ".",
                    TNCException.TNC_RESULT_ALREADY_INITIALIZED);
        }
    }

    @Override
    public void terminate() throws TNCException {
        checkInitialization();

        this.sessions.terminate();

        this.evaluatorManager.terminate();

        this.parameter.setPrimaryId(HSBConstants.HSB_IM_ID_UNKNOWN);
        this.parameter
                .setSupportedMessageTypes(new HashSet<SupportedMessageType>());

        this.tncs = null;
    }

    @Override
    public void notifyConnectionChange(final IMVConnection c,
            final long newState)
            throws TNCException {
        checkInitialization();

        TncConnectionState state =
                DefaultTncConnectionStateFactory.getInstance()
                .fromId(newState);

        if (state != null) {
            this.findSessionByConnection(c).setConnectionState(state);
        } else {
            throw new TNCException("Connection state "
                            + newState
                            + " is not a valid state.",
                    TNCException.TNC_RESULT_INVALID_PARAMETER);
        }

    }

    @Override
    public void receiveMessage(final IMVConnection c,
            final long messageType, final byte[] message)
            throws TNCException {
        checkInitialization();

        if (message != null && message.length > 0) {
            try {

                ImObjectComponent component = this
                        .receiveMessage(ImComponentFactory
                                .createLegacyRawComponent(
                                        messageType, message));

                this.findSessionByConnection(c).handleMessage(component);
            } catch (TncException e) {
                throw new TNCException(e.getMessage(), e.getResultCode().id());
            }
        }
    }

    @Override
    public void batchEnding(final IMVConnection c) throws TNCException {
        checkInitialization();
        try {
            this.findSessionByConnection(c).triggerMessage(
                    ImMessageTriggerEnum.BATCH_ENDING);
        } catch (TncException e) {
            throw new TNCException(e.getMessage(), e.getResultCode().id());
        }
    }

    @Override
    public void solicitRecommendation(final IMVConnection c)
            throws TNCException {
        checkInitialization();
        try {
            this.findSessionByConnection(c).solicitRecommendation();
        } catch (TncException e) {
            throw new TNCException(e.getMessage(), e.getResultCode().id());
        }

    }

    /**
     * Finds a session to the given connection, if one exists. If not
     * it creates a new session. Especially important for inheritance.
     *
     * @param connection the connection
     * @return the session for the connection
     */
    protected ImvSession findSessionByConnection(
            final IMVConnection connection) {
        ImvSession s = this.sessions.getSession(connection);

        if (s == null) {

            s = this.sessionFactory.createSession(
                    this.connectionFactory.createConnectionAdapter(connection),
                    evaluatorManager);

            this.sessions.putSession(connection, s);
        }

        return s;
    }

    /**
     * Checks if the IMV is initialized properly.
     * Especially important for inheritance.
     *
     * @throws TNCException if not initialized
     */
    protected void checkInitialization() throws TNCException {
        if (this.tncs == null) {
            throw new TNCException("IMV is not initialized.",
                    TNCException.TNC_RESULT_NOT_INITIALIZED);
        }
    }

    /**
     * Sets the primary ID parameter after initialization.
     * Especially important for inheritance.
     *
     * @param id the primary ID.
     */
    protected void setPrimaryId(final long id) {
        this.parameter.setPrimaryId(id);
    }
}
