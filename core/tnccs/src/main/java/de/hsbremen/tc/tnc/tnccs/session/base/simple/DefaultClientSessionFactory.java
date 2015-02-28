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
package de.hsbremen.tc.tnc.tnccs.session.base.simple;

import java.util.concurrent.Executors;

import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.message.TcgProtocolBindingIdentifier;
import de.hsbremen.tc.tnc.message.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.message.tnccs.serialize.TnccsBatchContainer;
import de.hsbremen.tc.tnc.message.tnccs.serialize.bytebuffer.TnccsReader;
import de.hsbremen.tc.tnc.message.tnccs.serialize.bytebuffer.TnccsWriter;
import de.hsbremen.tc.tnc.tnccs.adapter.connection.ImcConnectionContext;
import de.hsbremen.tc.tnc.tnccs.adapter.connection
.simple.DefaultImcConnectionContext;
import de.hsbremen.tc.tnc.tnccs.adapter.im.ImcAdapter;
import de.hsbremen.tc.tnc.tnccs.im.manager.ImAdapterManager;
import de.hsbremen.tc.tnc.tnccs.message.handler.ImcHandler;
import de.hsbremen.tc.tnc.tnccs.message.handler.ImcHandlerBuilder;
import de.hsbremen.tc.tnc.tnccs.message.handler.TnccContentHandler;
import de.hsbremen.tc.tnc.tnccs.message.handler.TnccContentHandlerFactory;
import de.hsbremen.tc.tnc.tnccs.message.handler.TnccHandler;
import de.hsbremen.tc.tnc.tnccs.message.handler.TnccsHandlerBuilder;
import de.hsbremen.tc.tnc.tnccs.message.handler.TnccsValidationExceptionHandler;
import de.hsbremen.tc.tnc.tnccs.message.handler.simple.DefaultImcHandlerBuilder;
import de.hsbremen.tc.tnc.tnccs.message.handler.simple
.DefaultTnccContentHandlerFactory;
import de.hsbremen.tc.tnc.tnccs.message.handler.simple
.DefaultTnccHandlerBuilder;
import de.hsbremen.tc.tnc.tnccs.message.handler.simple
.DefaultTnccsValidationExceptionHandlerBuilder;
import de.hsbremen.tc.tnc.tnccs.session.base.AttributeCollection;
import de.hsbremen.tc.tnc.tnccs.session.base.Session;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.StateHelper;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.StateMachine;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.StateMachineBuilder;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.simple
.DefaultClientStateHelper;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.simple
.DefaultClientStateMachineBuilder;
import de.hsbremen.tc.tnc.transport.TransportConnection;
import de.hsbremen.tc.tnc.util.NotNull;

/**
 * Default TNCC session factory.
 *
 * @author Carl-Heinz Genzel
 *
 */
public class DefaultClientSessionFactory extends AbstractSessionFactory {

    private final ImcHandlerBuilder imcHandlerBuilder;
    private final TnccsHandlerBuilder<TnccHandler> tnccHandlerBuilder;
    private final TnccsHandlerBuilder<TnccsValidationExceptionHandler>
    tnccsValExBuilder;
    private final TnccContentHandlerFactory tnccContentHandlerFactory;
    private final StateMachineBuilder<TnccContentHandler> stateMachineBuilder;

    /**
     * Creates a default TNCC session factory using the given
     * writer and reader as well as an adapter manager to adapt the
     * IMV available to a session.
     *
     * @param tnccsProtocol the IF-TNCCS protocol identifier
     * @param writer the message writer
     * @param reader the message reader
     * @param adapterManager the IMC adapter manager
     */
    public DefaultClientSessionFactory(
            final TcgProtocolBindingIdentifier tnccsProtocol,
            final TnccsWriter<TnccsBatch> writer,
            final TnccsReader<TnccsBatchContainer> reader,
            final ImAdapterManager<ImcAdapter> adapterManager) {
        this(tnccsProtocol, writer, reader,
                new DefaultImcHandlerBuilder(adapterManager),
                new DefaultTnccHandlerBuilder(),
                new DefaultTnccsValidationExceptionHandlerBuilder(),
                new DefaultTnccContentHandlerFactory(),
                new DefaultClientStateMachineBuilder());
    }

    /**
     * Creates a default TNCC session factory using the given
     * writer and reader. Several builders are added to
     * customize the session behavior.
     *
     * @param tnccsProtocol the IF-TNCCS protocol identifier
     * @param writer the message writer
     * @param reader the message reader
     * @param imcHandlerBuilder the builder to build an IMC message handler
     * @param tnccHandlerBuilder the builder to build an TNCC message handler
     * @param tnccsValExBuilder the builder to build an validation exception
     * @param tnccContentHandlerFactory the builder to build a content handler,
     * which manages the IMC, TNCC and validation exception handler
     * @param stateMachineBuilder the builder to build a client state machine
     */
    public DefaultClientSessionFactory(
            final TcgProtocolBindingIdentifier tnccsProtocol,
            final TnccsWriter<TnccsBatch> writer,
            final TnccsReader<TnccsBatchContainer> reader,
            final ImcHandlerBuilder imcHandlerBuilder,
            final TnccsHandlerBuilder<TnccHandler> tnccHandlerBuilder,
            final TnccsHandlerBuilder<TnccsValidationExceptionHandler>
            tnccsValExBuilder,
            final TnccContentHandlerFactory tnccContentHandlerFactory,
            final StateMachineBuilder<TnccContentHandler> stateMachineBuilder) {

            super(tnccsProtocol, writer, reader);
            NotNull.check("Constructor agruments cannot be null.",
                    imcHandlerBuilder, tnccHandlerBuilder, tnccsValExBuilder,
                    tnccContentHandlerFactory, stateMachineBuilder);

        this.imcHandlerBuilder = imcHandlerBuilder;
        this.tnccHandlerBuilder = tnccHandlerBuilder;
        this.tnccsValExBuilder = tnccsValExBuilder;
        this.tnccContentHandlerFactory = tnccContentHandlerFactory;
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

        ImcConnectionContext connectionContext =
                new DefaultImcConnectionContext(attributes, s);
        this.imcHandlerBuilder.setConnectionContext(connectionContext);
        ImcHandler imcHandler = this.imcHandlerBuilder.toHandler();

        this.tnccHandlerBuilder.setAttributes(attributes);
        TnccHandler tnccHandler = this.tnccHandlerBuilder.toHandler();

        this.tnccsValExBuilder.setAttributes(attributes);
        TnccsValidationExceptionHandler exceptionHandler =
                this.tnccsValExBuilder.toHandler();

        TnccContentHandler contentHandler = this.tnccContentHandlerFactory
                .createHandler(imcHandler, tnccHandler, exceptionHandler);

        StateHelper<TnccContentHandler> clientStateFactory =
                new DefaultClientStateHelper(attributes, contentHandler);
        this.stateMachineBuilder.setStateHelper(clientStateFactory);
        StateMachine machine = this.stateMachineBuilder.toStateMachine();

        // finalize session and run
        s.registerStatemachine(machine);
        s.registerConnection(connection);

        return s;

    }

}
