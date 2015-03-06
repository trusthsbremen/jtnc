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
package de.hsbremen.tc.tnc.im.adapter.imc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trustedcomputinggroup.tnc.ifimc.AttributeSupport;
import org.trustedcomputinggroup.tnc.ifimc.IMCConnection;
import org.trustedcomputinggroup.tnc.ifimc.IMCLong;
import org.trustedcomputinggroup.tnc.ifimc.TNCC;
import org.trustedcomputinggroup.tnc.ifimc.TNCException;

import de.hsbremen.tc.tnc.attribute.TncClientAttributeTypeEnum;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.im.adapter.ImParameter;
import de.hsbremen.tc.tnc.im.adapter.connection.ImcConnectionAdapterFactory;
import de.hsbremen.tc.tnc.im.adapter.data.ImComponentFactory;
import de.hsbremen.tc.tnc.im.adapter.data.ImObjectComponent;
import de.hsbremen.tc.tnc.im.adapter.tncc.TnccAdapterFactory;
import de.hsbremen.tc.tnc.im.evaluate.ImEvaluatorFactory;
import de.hsbremen.tc.tnc.im.session.ImSessionFactory;
import de.hsbremen.tc.tnc.im.session.ImcSession;
import de.hsbremen.tc.tnc.im.session.ImSessionManager;
import de.hsbremen.tc.tnc.message.m.serialize.ImMessageContainer;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImReader;

/**
 * IMC adapter according to IETF/TCG specifications.
 * Implementing an IF-IMC IMC interface with long addressing
 * support.
 *
 *
 */
public class ImcAdapterIetfLong extends ImcAdapterIetf implements IMCLong {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(ImcAdapterIetfLong.class);

    /**
     * Creates an IMC adapter with default arguments. This constructor
     * is specified to be called to load an IMC from a source.
     *
     * A specific IMC must override this constructor and add its custom
     * arguments. This implementation of the constructor should only be
     * used for testing purpose.
     */
    public ImcAdapterIetfLong() {
        super();
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
    public ImcAdapterIetfLong(final ImParameter parameter,
            final TnccAdapterFactory tnccFactory,
            final ImSessionFactory<ImcSession> sessionFactory,
            final ImSessionManager<IMCConnection, ImcSession> sessionManager,
            final ImEvaluatorFactory evaluatorFactory,
            final ImcConnectionAdapterFactory connectionFactory,
            final ImReader<? extends ImMessageContainer> imReader) {
        super(parameter, tnccFactory, sessionFactory, sessionManager,
                evaluatorFactory, connectionFactory, imReader);

    }

    @Override
    public void initialize(final TNCC tncc) throws TNCException {
        if (tncc instanceof AttributeSupport) {
            try {
                Object o = ((AttributeSupport) tncc).getAttribute(
                               TncClientAttributeTypeEnum
                               .TNC_ATTRIBUTEID_PRIMARY_IMC_ID.id());

                if (o instanceof Long) {
                    long id = ((Long) o).longValue();
                    super.setPrimaryId(id);
                }

            } catch (TNCException | UnsupportedOperationException e) {
                LOGGER.warn(
                        "Primary ID initalization failed, "
                        + "this IMC will only work in basic mode.",
                        e);
            }

        }
        super.initialize(tncc);
    }

    @Override
    public void receiveMessageLong(final IMCConnection c,
            final long messageFlags,
            final long messageVendorID, final long messageSubtype,
            final byte[] message,
            final long sourceIMVID, final long destinationIMCID)
            throws TNCException {
        super.checkInitialization();

        try {
            final int messageFlagsMask = 0xFF;

            ImObjectComponent component = super
                    .receiveMessage(
                            ImComponentFactory.createRawComponent(
                            (byte) (messageFlags & messageFlagsMask),
                            messageVendorID, messageSubtype,
                            destinationIMCID, sourceIMVID,
                            message));

            super.findSessionByConnection(c).handleMessage(component);

        } catch (TncException e) {
            throw new TNCException(e.getMessage(), e.getResultCode().id());
        }
    }
}
