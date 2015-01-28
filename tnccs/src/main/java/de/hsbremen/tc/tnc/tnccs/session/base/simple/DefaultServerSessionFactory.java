package de.hsbremen.tc.tnc.tnccs.session.base.simple;

import java.util.concurrent.Executors;

import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.message.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.message.tnccs.serialize.TnccsBatchContainer;
import de.hsbremen.tc.tnc.message.tnccs.serialize.bytebuffer.TnccsReader;
import de.hsbremen.tc.tnc.message.tnccs.serialize.bytebuffer.TnccsWriter;
import de.hsbremen.tc.tnc.tnccs.adapter.connection.ImvConnectionContext;
import de.hsbremen.tc.tnc.tnccs.adapter.connection.simple.DefaultImvConnectionContext;
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
import de.hsbremen.tc.tnc.tnccs.message.handler.simple.DefaultTnccsValidationExceptionHandlerBuilder;
import de.hsbremen.tc.tnc.tnccs.message.handler.simple.DefaultTncsContentHandlerFactory;
import de.hsbremen.tc.tnc.tnccs.message.handler.simple.DefaultTncsHandlerBuilder;
import de.hsbremen.tc.tnc.tnccs.session.base.AttributeCollection;
import de.hsbremen.tc.tnc.tnccs.session.base.Session;
import de.hsbremen.tc.tnc.tnccs.session.base.SessionFactory;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.StateHelper;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.StateMachine;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.StateMachineBuilder;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.simple.DefaultServerStateFactory;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.simple.DefaultServerStateMachineBuilder;
import de.hsbremen.tc.tnc.transport.TransportConnection;

public class DefaultServerSessionFactory implements SessionFactory {

	private final String tnccsProtocolId;
	private final String tnccsProtocolVersion;
	
	private final TnccsWriter<TnccsBatch> writer;
	private final TnccsReader<TnccsBatchContainer> reader;
	
	private final ImvHandlerBuilder imvHandlerBuilder;
	private final TnccsHandlerBuilder<TncsHandler> tncsHandlerBuilder;
	private final TnccsHandlerBuilder<TnccsValidationExceptionHandler> tnccsValExBuilder;
	private final TncsContentHandlerFactory tncsContentHandlerFactory;
	private final StateMachineBuilder<TncsContentHandler> stateMachineBuilder;
	
	public DefaultServerSessionFactory(
			String tnccsProtocolId, 
			String tnccsProtocolVersion, 
			TnccsWriter<TnccsBatch> writer, 
			TnccsReader<TnccsBatchContainer> reader, ImAdapterManager<ImvAdapter> adapterManager){
		this(tnccsProtocolId, tnccsProtocolVersion, writer, reader,
				new DefaultImvHandlerBuilder(adapterManager), 
				new DefaultTncsHandlerBuilder(), 
				new DefaultTnccsValidationExceptionHandlerBuilder(), 
				new DefaultTncsContentHandlerFactory(), 
				new DefaultServerStateMachineBuilder());
	}
	
	public DefaultServerSessionFactory(
			String tnccsProtocolId, 
			String tnccsProtocolVersion, 
			TnccsWriter<TnccsBatch> writer, 
			TnccsReader<TnccsBatchContainer> reader,
			ImvHandlerBuilder imvHandlerBuilder,
			TnccsHandlerBuilder<TncsHandler> tncsHandlerBuilder,
			TnccsHandlerBuilder<TnccsValidationExceptionHandler> tnccsValExBuilder,
			TncsContentHandlerFactory tncsContentHandlerFactory,
			StateMachineBuilder<TncsContentHandler> stateMachineBuilder){
		
		this.tnccsProtocolId = tnccsProtocolId;
		this.tnccsProtocolVersion = tnccsProtocolVersion;
		
		this.reader = reader;
		this.writer = writer;
		
		this.imvHandlerBuilder = imvHandlerBuilder;
		this.tncsHandlerBuilder = tncsHandlerBuilder;
		this.tnccsValExBuilder = tnccsValExBuilder;
		this.tncsContentHandlerFactory = tncsContentHandlerFactory;
		this.stateMachineBuilder = stateMachineBuilder;
	}
	

	/**
	 * @return the tnccsProtocolId
	 */
	public String getTnccsProtocolId() {
		return this.tnccsProtocolId;
	}



	/**
	 * @return the tnccsProtocolVersion
	 */
	public String getTnccsProtocolVersion() {
		return this.tnccsProtocolVersion;
	}



	/**
	 * @return the writer
	 */
	public TnccsWriter<TnccsBatch> getWriter() {
		return this.writer;
	}



	/**
	 * @return the reader
	 */
	public TnccsReader<TnccsBatchContainer> getReader() {
		return this.reader;
	}



	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.tnccs.session.base.SessionFactory#createTnccSession(de.hsbremen.tc.tnc.transport.connection.TransportConnection)
	 */
	@Override
	public Session createTnccsSession(TransportConnection connection){
		
		DefaultSession s = new DefaultSession(new DefaultSessionAttributes(this.tnccsProtocolId, this.tnccsProtocolVersion), this.writer, this.reader, Executors.newSingleThreadExecutor());
		
		AttributeCollection attributes = new AttributeCollection();
		Attributed sessionAttributes = s.getAttributes();
		if(sessionAttributes != null){
			attributes.add(s.getAttributes());
		}
		
		Attributed connectionAttributes = connection.getAttributes();
		if(connectionAttributes != null){
			attributes.add(connectionAttributes);
		}
		
		ImvConnectionContext connectionContext = new DefaultImvConnectionContext(attributes,s);
		this.imvHandlerBuilder.setConnectionContext(connectionContext);
		ImvHandler imvHandler = this.imvHandlerBuilder.toHandler();
		
		this.tncsHandlerBuilder.setAttributes(attributes);
		TncsHandler tncsHandler = this.tncsHandlerBuilder.toHandler();
		
		this.tnccsValExBuilder.setAttributes(attributes);
		TnccsValidationExceptionHandler exceptionHandler = this.tnccsValExBuilder.toHandler();
		
		TncsContentHandler contentHandler = this.tncsContentHandlerFactory.createHandler(imvHandler, tncsHandler, exceptionHandler);
		
		StateHelper<TncsContentHandler> serverStateFactory = new DefaultServerStateFactory(attributes,contentHandler);
		this.stateMachineBuilder.setStateHelper(serverStateFactory);
		StateMachine machine = this.stateMachineBuilder.toStateMachine();
		
		// finalize session and run
		s.registerStatemachine(machine);
		s.registerConnection(connection);
		
		return s;
		
	}

}
