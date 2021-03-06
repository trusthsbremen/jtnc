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
import de.hsbremen.tc.tnc.im.adapter.tnccs.TnccAdapterFactory;
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
