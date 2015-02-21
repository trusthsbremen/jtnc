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
 * @author Carl-Heinz Genzel
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
     * @param tnccsProtocolType the IF-TNCCS protocol type
     * @param tnccsProtocolVersion the if TNCCS protocol version
     * @param writer the message writer
     * @param reader the message reader
     * @param adapterManager the IMV adapter manager
     */
    public DefaultServerSessionFactory(final String tnccsProtocolType,
            final String tnccsProtocolVersion,
            final TnccsWriter<TnccsBatch> writer,
            final TnccsReader<TnccsBatchContainer> reader,
            final ImAdapterManager<ImvAdapter> adapterManager) {
        this(tnccsProtocolType, tnccsProtocolVersion, writer, reader,
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
     * @param tnccsProtocolType the IF-TNCCS protocol type
     * @param tnccsProtocolVersion the if TNCCS protocol version
     * @param writer the message writer
     * @param reader the message reader
     * @param imvHandlerBuilder the builder to build an IMV message handler
     * @param tncsHandlerBuilder the builder to build an TNCS message handler
     * @param tnccsValExBuilder  the builder to build an validation exception
     * handler
     * @param tncsContentHandlerFactory the builder to build a content handler,
     * which manages the IMV, TNCS and validation exception handler
     * @param stateMachineBuilder the builder to build a server state machine
     */
    public DefaultServerSessionFactory(
            final String tnccsProtocolType,
            final String tnccsProtocolVersion,
            final TnccsWriter<TnccsBatch> writer,
            final TnccsReader<TnccsBatchContainer> reader,
            final ImvHandlerBuilder imvHandlerBuilder,
            final TnccsHandlerBuilder<TncsHandler> tncsHandlerBuilder,
            final TnccsHandlerBuilder<TnccsValidationExceptionHandler>
            tnccsValExBuilder,
            final TncsContentHandlerFactory tncsContentHandlerFactory,
            final StateMachineBuilder<TncsContentHandler> stateMachineBuilder) {
        super(tnccsProtocolType, tnccsProtocolVersion, writer, reader);
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
                super.getTnccsProtocolType(), super.getTnccsProtocolVersion()),
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
