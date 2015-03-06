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
package de.hsbremen.tc.tnc.im.adapter.imv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trustedcomputinggroup.tnc.ifimv.IMVConnection;
import org.trustedcomputinggroup.tnc.ifimv.IMVLong;
import org.trustedcomputinggroup.tnc.ifimv.TNCException;
import org.trustedcomputinggroup.tnc.ifimv.TNCS;

import de.hsbremen.tc.tnc.attribute.TncClientAttributeTypeEnum;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.im.adapter.ImParameter;
import de.hsbremen.tc.tnc.im.adapter.connection.ImvConnectionAdapterFactory;
import de.hsbremen.tc.tnc.im.adapter.data.ImComponentFactory;
import de.hsbremen.tc.tnc.im.adapter.data.ImObjectComponent;
import de.hsbremen.tc.tnc.im.adapter.tncs.TncsAdapterFactory;
import de.hsbremen.tc.tnc.im.evaluate.ImEvaluatorFactory;
import de.hsbremen.tc.tnc.im.session.ImSessionFactory;
import de.hsbremen.tc.tnc.im.session.ImvSession;
import de.hsbremen.tc.tnc.im.session.ImSessionManager;
import de.hsbremen.tc.tnc.message.m.serialize.ImMessageContainer;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImReader;

/**
 * IMV adapter according to IETF/TCG specifications.
 * Implementing an IF-IMV IMV interface with long addressing
 * support.
 *
 *
 */
public class ImvAdapterIetfLong extends ImvAdapterIetf implements IMVLong {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(ImvAdapterIetfLong.class);

    /**
     * Creates an IMV adapter with default arguments. This constructor
     * is specified to be called to load an IMV from a source.
     *
     * A specific IMV must override this constructor and add its custom
     * arguments. This implementation of the constructor should only be
     * used for testing purpose.
     */
    public ImvAdapterIetfLong() {
        super();
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
    public ImvAdapterIetfLong(final ImParameter parameter,
            final TncsAdapterFactory tncsFactory,
            final ImSessionFactory<ImvSession> sessionFactory,
            final ImSessionManager<IMVConnection, ImvSession> sessionManager,
            final ImEvaluatorFactory evaluatorFactory,
            final ImvConnectionAdapterFactory connectionFactory,
            final ImReader<? extends ImMessageContainer> imReader) {
        super(parameter, tncsFactory, sessionFactory, sessionManager,
                evaluatorFactory, connectionFactory, imReader);
    }

    @Override
    public void initialize(final TNCS tncs) throws TNCException {

        try {

            Object o = tncs.getAttribute(
                    TncClientAttributeTypeEnum.
                    TNC_ATTRIBUTEID_PRIMARY_IMC_ID.id());

            if (o instanceof Long) {

                long id = ((Long) o).longValue();
                super.setPrimaryId(id);
            }

        } catch (TNCException | UnsupportedOperationException e) {
            LOGGER.warn(
                    "Primary ID initalization failed, "
                    + "this IMV will work with reduced functionality.",
                    e);
        }

        super.initialize(tncs);
    }

    @Override
    public void receiveMessageLong(final IMVConnection c,
            final long messageFlags,
            final long messageVendorID, final long messageSubtype,
            final byte[] message,
            final long sourceIMVID, final long destinationIMCID)
                    throws TNCException {
        super.checkInitialization();

        try {
            final int messageFlagsMask = 0xFF;

            ImObjectComponent component = super
                    .receiveMessage(ImComponentFactory.createRawComponent(
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
