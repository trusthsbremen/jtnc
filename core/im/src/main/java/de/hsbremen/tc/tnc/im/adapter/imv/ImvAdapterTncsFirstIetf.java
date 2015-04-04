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
package de.hsbremen.tc.tnc.im.adapter.imv;

import org.ietf.nea.pa.serialize.reader.bytebuffer.PaReaderFactory;
import org.ietf.nea.pa.serialize.writer.bytebuffer.PaWriterFactory;
import org.trustedcomputinggroup.tnc.ifimv.IMVConnection;
import org.trustedcomputinggroup.tnc.ifimv.IMVTNCSFirst;
import org.trustedcomputinggroup.tnc.ifimv.TNCException;

import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.im.adapter.ImParameter;
import de.hsbremen.tc.tnc.im.adapter.connection.ImvConnectionAdapterFactory;
import de.hsbremen.tc.tnc.im.adapter.connection.ImvConnectionAdapterFactoryIetf;
import de.hsbremen.tc.tnc.im.adapter.tnccs.TncsAdapterFactory;
import de.hsbremen.tc.tnc.im.adapter.tnccs.TncsAdapterFactoryIetf;
import de.hsbremen.tc.tnc.im.evaluate.ImEvaluatorFactory;
import de.hsbremen.tc.tnc.im.evaluate.simple.DefaultImcEvaluatorFactory;
import de.hsbremen.tc.tnc.im.session.DefaultImvSessionFactory;
import de.hsbremen.tc.tnc.im.session.ImSessionFactory;
import de.hsbremen.tc.tnc.im.session.ImvSession;
import de.hsbremen.tc.tnc.im.session.DefaultImSessionManager;
import de.hsbremen.tc.tnc.im.session.ImSessionManager;
import de.hsbremen.tc.tnc.im.session.enums.ImMessageTriggerEnum;
import de.hsbremen.tc.tnc.message.m.serialize.ImMessageContainer;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImReader;

/**
 * IMV adapter according to IETF/TCG specifications. Implementing a simple
 * IF-IMV IMV interface with TNCS first support.
 *
 *
 */
public class ImvAdapterTncsFirstIetf extends ImvAdapterIetf implements
        IMVTNCSFirst {

    /**
     * Creates an IMV adapter with default arguments. This constructor is
     * specified to be called to load an IMV from a source.
     *
     * A specific IMV must override this constructor and add its custom
     * arguments. This implementation of the constructor should only be used for
     * testing purpose.
     */
    public ImvAdapterTncsFirstIetf() {
        this(new ImParameter(true), new TncsAdapterFactoryIetf(),
                new DefaultImvSessionFactory(),
                new DefaultImSessionManager<IMVConnection, ImvSession>(),
                new DefaultImcEvaluatorFactory(),
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
    public ImvAdapterTncsFirstIetf(final ImParameter parameter,
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
    public void beginHandshake(final IMVConnection c) throws TNCException {

        super.checkInitialization();
        try {
            super.findSessionByConnection(c).triggerMessage(
                    ImMessageTriggerEnum.BEGIN_HANDSHAKE);
        } catch (TncException e) {
            throw new TNCException(e.getMessage(), e.getResultCode().id());

        }

    }
}
