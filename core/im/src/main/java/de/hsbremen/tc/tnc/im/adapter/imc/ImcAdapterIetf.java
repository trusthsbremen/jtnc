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
package de.hsbremen.tc.tnc.im.adapter.imc;

import java.util.HashSet;

import org.ietf.nea.pa.serialize.reader.bytebuffer.PaReaderFactory;
import org.ietf.nea.pa.serialize.writer.bytebuffer.PaWriterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trustedcomputinggroup.tnc.ifimc.AttributeSupport;
import org.trustedcomputinggroup.tnc.ifimc.IMC;
import org.trustedcomputinggroup.tnc.ifimc.IMCConnection;
import org.trustedcomputinggroup.tnc.ifimc.TNCC;
import org.trustedcomputinggroup.tnc.ifimc.TNCException;

import de.hsbremen.tc.tnc.HSBConstants;
import de.hsbremen.tc.tnc.attribute.TncCommonAttributeTypeEnum;
import de.hsbremen.tc.tnc.connection.DefaultTncConnectionStateFactory;
import de.hsbremen.tc.tnc.connection.TncConnectionState;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.im.adapter.AbstractImAdapter;
import de.hsbremen.tc.tnc.im.adapter.ImParameter;
import de.hsbremen.tc.tnc.im.adapter.connection.ImcConnectionAdapterFactory;
import de.hsbremen.tc.tnc.im.adapter.connection.ImcConnectionAdapterFactoryIetf;
import de.hsbremen.tc.tnc.im.adapter.data.ImComponentFactory;
import de.hsbremen.tc.tnc.im.adapter.data.ImObjectComponent;
import de.hsbremen.tc.tnc.im.adapter.tnccs.TnccAdapter;
import de.hsbremen.tc.tnc.im.adapter.tnccs.TnccAdapterFactory;
import de.hsbremen.tc.tnc.im.adapter.tnccs.TnccAdapterFactoryIetf;
import de.hsbremen.tc.tnc.im.evaluate.ImEvaluatorFactory;
import de.hsbremen.tc.tnc.im.evaluate.ImEvaluatorManager;
import de.hsbremen.tc.tnc.im.evaluate.simple.DefaultImcEvaluatorFactory;
import de.hsbremen.tc.tnc.im.session.DefaultImcSessionFactory;
import de.hsbremen.tc.tnc.im.session.ImSessionFactory;
import de.hsbremen.tc.tnc.im.session.ImcSession;
import de.hsbremen.tc.tnc.im.session.DefaultImSessionManager;
import de.hsbremen.tc.tnc.im.session.ImSessionManager;
import de.hsbremen.tc.tnc.im.session.enums.ImMessageTriggerEnum;
import de.hsbremen.tc.tnc.message.m.serialize.ImMessageContainer;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImReader;
import de.hsbremen.tc.tnc.report.SupportedMessageType;

/**
 * IMC adapter according to IETF/TCG specifications.
 * Implementing a simple IF-IMC IMC interface.
 *
 *
 */
public class ImcAdapterIetf extends AbstractImAdapter
    implements IMC, AttributeSupport {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(ImcAdapterIetf.class);

    private final ImParameter parameter;

    private final TnccAdapterFactory tnccFactory;
    private final ImcConnectionAdapterFactory connectionFactory;

    private final ImSessionFactory<ImcSession> sessionFactory;
    private final ImSessionManager<IMCConnection, ImcSession> sessions;

    private final ImEvaluatorFactory evaluatorFactory;
    private ImEvaluatorManager evaluatorManager;

    private TnccAdapter tncc;

    /**
     * Creates an IMC adapter with default arguments. This constructor
     * is specified to be called to load an IMC from a source.
     *
     * A specific IMC must override this constructor and add its custom
     * arguments. This implementation of the constructor should only be
     * used for testing purpose.
     */
    public ImcAdapterIetf() {
        this(new ImParameter(), new TnccAdapterFactoryIetf(),
                new DefaultImcSessionFactory(),
                new DefaultImSessionManager<IMCConnection, ImcSession>(),
                new DefaultImcEvaluatorFactory(),
                new ImcConnectionAdapterFactoryIetf(
                        PaWriterFactory.createProductionDefault()),
                PaReaderFactory.createProductionDefault());
    }

    /**
     * Creates an IMC adapter with the specified arguments. This constructor
     * is especially for inheritance.
     *
     * @param parameter the generic IMC parameter
     * @param tnccFactory the TNCC adapter factory
     * @param sessionFactory the factory for a connection session
     * @param sessionManager the session manager
     * @param evaluatorFactory the factory to instantiate the evaluation system
     * @param connectionFactory the connection adapter factory
     * @param imReader the integrity message reader
     */
    public ImcAdapterIetf(final ImParameter parameter,
            final TnccAdapterFactory tnccFactory,
            final ImSessionFactory<ImcSession> sessionFactory,
            final ImSessionManager<IMCConnection, ImcSession> sessionManager,
            final ImEvaluatorFactory evaluatorFactory,
            final ImcConnectionAdapterFactory connectionFactory,
            final ImReader<? extends ImMessageContainer> imReader) {
        super(imReader);

        this.parameter = parameter;

        this.tnccFactory = tnccFactory;
        this.connectionFactory = connectionFactory;
        this.evaluatorFactory = evaluatorFactory;
        this.sessionFactory = sessionFactory;

        this.sessions = sessionManager;
    }

    @Override
    public void initialize(final TNCC tncc) throws TNCException {
        if (this.tncc == null) {
            this.tncc = this.tnccFactory.createTnccAdapter(this, tncc);
            this.evaluatorManager = this.evaluatorFactory.getEvaluators(
                    this.tncc, this.parameter);
            try {

                this.tncc.reportMessageTypes(this.evaluatorManager
                        .getSupportedMessageTypes());

            } catch (TncException e) {
                throw new TNCException(e.getMessage(), e.getResultCode().id());
            }

            if (tncc instanceof AttributeSupport) {
                try {
                    Object o = ((AttributeSupport) tncc)
                            .getAttribute(TncCommonAttributeTypeEnum
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
            }

            this.sessions.initialize();

        } else {
            throw new TNCException("IMC already initialized by "
                    + this.tncc.toString() + ".",
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

        this.tncc = null;
    }

    @Override
    public void notifyConnectionChange(final IMCConnection c,
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
    public void beginHandshake(final IMCConnection c) throws TNCException {
        checkInitialization();
        try {

            this.findSessionByConnection(c).triggerMessage(
                    ImMessageTriggerEnum.BEGIN_HANDSHAKE);

        } catch (TncException e) {
            throw new TNCException(e.getMessage(), e.getResultCode().id());
        }

    }

    @Override
    public void receiveMessage(final IMCConnection c,
            final long messageType, final byte[] message)
            throws TNCException {
        checkInitialization();

        if (message != null && message.length > 0) {
            try {

                ImObjectComponent component = super
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
    public void batchEnding(final IMCConnection c) throws TNCException {
        checkInitialization();
        try {

            this.findSessionByConnection(c).triggerMessage(
                    ImMessageTriggerEnum.BATCH_ENDING);

        } catch (TncException e) {
            throw new TNCException(e.getMessage(), e.getResultCode().id());
        }
    }

    @Override
    public Object getAttribute(final long attributeID) throws TNCException {

        if (attributeID == AttributeSupport.TNC_ATTRIBUTEID_IMC_SPTS_TNCS1) {
            return (this.parameter.hasTncsFirstSupport()) ? Boolean.TRUE
                    : Boolean.FALSE;
        }

        throw new TNCException("The attribute with ID " + attributeID
                + " is unknown.", TNCException.TNC_RESULT_INVALID_PARAMETER);

    }

    @Override
    public void setAttribute(final long attributeID,
            final Object attributeValue)
            throws TNCException {

        throw new UnsupportedOperationException(
                "The operation setAttribute(...) is not supported, "
                + "because there are no attributes to set.");

    }

    /**
     * Finds a session to the given connection, if one exists. If not
     * it creates a new session. Especially important for inheritance.
     *
     * @param connection the connection
     * @return the session for the connection
     */
    protected ImcSession findSessionByConnection(
            final IMCConnection connection) {

        ImcSession s = this.sessions.getSession(connection);

        if (s == null) {

            s = this.sessionFactory.createSession(
                    this.connectionFactory.createConnectionAdapter(connection),
                    evaluatorManager);
            this.sessions.putSession(connection, s);
        }

        return s;
    }

    /**
     * Checks if the IMC is initialized properly.
     * Especially important for inheritance.
     *
     * @throws TNCException if not initialized
     */
    protected void checkInitialization() throws TNCException {
        if (this.tncc == null) {
            throw new TNCException("IMC is not initialized.",
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
