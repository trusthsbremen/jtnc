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
package de.hsbremen.tc.tnc.tnccs.session.base.simple;

import java.util.concurrent.Executors;

import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.message.TcgProtocolBindingIdentifier;
import de.hsbremen.tc.tnc.message.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.message.tnccs.serialize.TnccsBatchContainer;
import de.hsbremen.tc.tnc.message.tnccs.serialize.bytebuffer.TnccsReader;
import de.hsbremen.tc.tnc.message.tnccs.serialize.bytebuffer.TnccsWriter;
import de.hsbremen.tc.tnc.tnccs.adapter.connection.ImvConnectionContext;
import de.hsbremen.tc.tnc.tnccs.adapter.connection.simple
.DefaultImvConnectionContext;
import de.hsbremen.tc.tnc.tnccs.adapter.im.ImvAdapter;
import de.hsbremen.tc.tnc.tnccs.im.manager.ImAdapterManager;
import de.hsbremen.tc.tnc.tnccs.message.handler.ImvHandler;
import de.hsbremen.tc.tnc.tnccs.message.handler.ImvHandlerBuilder;
import de.hsbremen.tc.tnc.tnccs.message.handler.TnccsHandlerBuilder;
import de.hsbremen.tc.tnc.tnccs.message.handler.TnccsValidationExceptionHandler;
import de.hsbremen.tc.tnc.tnccs.message.handler.TncsContentHandler;
import de.hsbremen.tc.tnc.tnccs.message.handler.TncsContentHandlerFactory;
import de.hsbremen.tc.tnc.tnccs.message.handler.TncsHandler;
import de.hsbremen.tc.tnc.tnccs.message.handler.simple.DefaultImvHandlerBuilder;
import de.hsbremen.tc.tnc.tnccs.message.handler.simple
.DefaultTnccsValidationExceptionHandlerBuilder;
import de.hsbremen.tc.tnc.tnccs.message.handler.simple
.DefaultTncsContentHandlerFactory;
import de.hsbremen.tc.tnc.tnccs.message.handler.simple
.DefaultTncsHandlerBuilder;
import de.hsbremen.tc.tnc.tnccs.session.base.AttributeCollection;
import de.hsbremen.tc.tnc.tnccs.session.base.Session;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.StateHelper;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.StateMachine;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.StateMachineBuilder;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.simple
.DefaultServerStateHelper;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.simple
.DefaultServerStateMachineBuilder;
import de.hsbremen.tc.tnc.transport.TransportConnection;
import de.hsbremen.tc.tnc.util.NotNull;

/**
 * Default TNCS session factory.
 *
 *
 */
public class DefaultServerSessionFactory extends AbstractSessionFactory {

    private final ImvHandlerBuilder imvHandlerBuilder;
    private final TnccsHandlerBuilder<TncsHandler> tncsHandlerBuilder;
    private final TnccsHandlerBuilder<TnccsValidationExceptionHandler>
    tnccsValExBuilder;
    private final TncsContentHandlerFactory tncsContentHandlerFactory;
    private final StateMachineBuilder<TncsContentHandler> stateMachineBuilder;

    /**
     * Creates a default TNCS session factory using the given
     * writer and reader as well as an adapter manager to adapt the
     * IMV available to a session.
     *
     * @param tnccsProtocol the IF-TNCCS protocol identifier
     * @param writer the message writer
     * @param reader the message reader
     * @param adapterManager the IMV adapter manager
     */
    public DefaultServerSessionFactory(
            final TcgProtocolBindingIdentifier tnccsProtocol,
            final TnccsWriter<TnccsBatch> writer,
            final TnccsReader<TnccsBatchContainer> reader,
            final ImAdapterManager<ImvAdapter> adapterManager) {
        this(tnccsProtocol, writer, reader,
                new DefaultImvHandlerBuilder(adapterManager),
                new DefaultTncsHandlerBuilder(),
                new DefaultTnccsValidationExceptionHandlerBuilder(),
                new DefaultTncsContentHandlerFactory(),
                new DefaultServerStateMachineBuilder());
    }

    /**
     * Creates a default TNCS session factory using the given
     * writer and reader. Several builders are added to
     * customize the session behavior.
     *
     * @param tnccsProtocol the IF-TNCCS protocol identifier
     * @param writer the message writer
     * @param reader the message reader
     * @param imvHandlerBuilder the builder to build an IMV message handler
     * @param tncsHandlerBuilder the builder to build a TNCS message handler
     * @param tnccsValExBuilder  the builder to build a validation exception
     * handler
     * @param tncsContentHandlerFactory the builder to build a content handler,
     * which manages the IMV, TNCS and validation exception handler
     * @param stateMachineBuilder the builder to build a server state machine
     */
    public DefaultServerSessionFactory(
            final TcgProtocolBindingIdentifier tnccsProtocol,
            final TnccsWriter<TnccsBatch> writer,
            final TnccsReader<TnccsBatchContainer> reader,
            final ImvHandlerBuilder imvHandlerBuilder,
            final TnccsHandlerBuilder<TncsHandler> tncsHandlerBuilder,
            final TnccsHandlerBuilder<TnccsValidationExceptionHandler>
            tnccsValExBuilder,
            final TncsContentHandlerFactory tncsContentHandlerFactory,
            final StateMachineBuilder<TncsContentHandler> stateMachineBuilder) {
        super(tnccsProtocol, writer, reader);
        NotNull.check("Constructor agruments cannot be null.",
                imvHandlerBuilder, tncsHandlerBuilder,
                tnccsValExBuilder, tncsContentHandlerFactory,
                stateMachineBuilder);

        this.imvHandlerBuilder = imvHandlerBuilder;
        this.tncsHandlerBuilder = tncsHandlerBuilder;
        this.tnccsValExBuilder = tnccsValExBuilder;
        this.tncsContentHandlerFactory = tncsContentHandlerFactory;
        this.stateMachineBuilder = stateMachineBuilder;
    }

    @Override
    public Session createTnccsSession(final TransportConnection connection) {

        DefaultSession s = new DefaultSession(new DefaultSessionAttributes(
                super.getTnccsProtocolIdentifier()),
                super.getWriter(), super.getReader(),
                Executors.newSingleThreadExecutor());

        AttributeCollection attributes = new AttributeCollection();
        Attributed sessionAttributes = s.getAttributes();
        if (sessionAttributes != null) {
            attributes.add(s.getAttributes());
        }

        Attributed connectionAttributes = connection.getAttributes();
        if (connectionAttributes != null) {
            attributes.add(connectionAttributes);
        }

        ImvConnectionContext connectionContext =
                new DefaultImvConnectionContext(attributes, s);
        this.imvHandlerBuilder.setConnectionContext(connectionContext);
        ImvHandler imvHandler = this.imvHandlerBuilder.toHandler();

        this.tncsHandlerBuilder.setAttributes(attributes);
        TncsHandler tncsHandler = this.tncsHandlerBuilder.toHandler();

        this.tnccsValExBuilder.setAttributes(attributes);
        TnccsValidationExceptionHandler exceptionHandler =
                this.tnccsValExBuilder.toHandler();

        TncsContentHandler contentHandler = this.tncsContentHandlerFactory
                .createHandler(imvHandler, tncsHandler, exceptionHandler);

        StateHelper<TncsContentHandler> serverStateFactory =
                new DefaultServerStateHelper(attributes, contentHandler);
        this.stateMachineBuilder.setStateHelper(serverStateFactory);
        StateMachine machine = this.stateMachineBuilder.toStateMachine();

        // finalize session and run
        s.registerStatemachine(machine);
        s.registerConnection(connection);

        return s;

    }

}
