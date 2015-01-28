package de.hsbremen.tc.tnc.tnccs.session.base.simple;

import java.util.concurrent.Executors;

import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.message.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.message.tnccs.serialize.TnccsBatchContainer;
import de.hsbremen.tc.tnc.message.tnccs.serialize.bytebuffer.TnccsReader;
import de.hsbremen.tc.tnc.message.tnccs.serialize.bytebuffer.TnccsWriter;
import de.hsbremen.tc.tnc.tnccs.adapter.connection.ImcConnectionContext;
import de.hsbremen.tc.tnc.tnccs.adapter.connection.simple.DefaultImcConnectionContext;
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
import de.hsbremen.tc.tnc.tnccs.message.handler.simple.DefaultTnccContentHandlerFactory;
import de.hsbremen.tc.tnc.tnccs.message.handler.simple.DefaultTnccHandlerBuilder;
import de.hsbremen.tc.tnc.tnccs.message.handler.simple.DefaultTnccsValidationExceptionHandlerBuilder;
import de.hsbremen.tc.tnc.tnccs.session.base.AttributeCollection;
import de.hsbremen.tc.tnc.tnccs.session.base.Session;
import de.hsbremen.tc.tnc.tnccs.session.base.SessionFactory;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.StateHelper;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.StateMachine;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.StateMachineBuilder;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.simple.DefaultClientStateFactory;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.simple.DefaultClientStateMachineBuilder;
import de.hsbremen.tc.tnc.transport.TransportConnection;

public class DefaultClientSessionFactory implements SessionFactory {

	private final String tnccsProtocolId;
	private final String tnccsProtocolVersion;
	
	private final TnccsWriter<TnccsBatch> writer;
	private final TnccsReader<TnccsBatchContainer> reader;
	
	private final ImcHandlerBuilder imcHandlerBuilder;
	private final TnccsHandlerBuilder<TnccHandler> tnccHandlerBuilder;
	private final TnccsHandlerBuilder<TnccsValidationExceptionHandler> tnccsValExBuilder;
	private final TnccContentHandlerFactory tnccContentHandlerFactory;
	private final StateMachineBuilder<TnccContentHandler> stateMachineBuilder;
	
	public DefaultClientSessionFactory(
			String tnccsProtocolId, 
			String tnccsProtocolVersion, 
			TnccsWriter<TnccsBatch> writer, 
			TnccsReader<TnccsBatchContainer> reader, ImAdapterManager<ImcAdapter> adapterManager){
		this(tnccsProtocolId, tnccsProtocolVersion, writer, reader,
				new DefaultImcHandlerBuilder(adapterManager), 
				new DefaultTnccHandlerBuilder(), 
				new DefaultTnccsValidationExceptionHandlerBuilder(), 
				new DefaultTnccContentHandlerFactory(), 
				new DefaultClientStateMachineBuilder());
	}
	
	public DefaultClientSessionFactory(
			String tnccsProtocolId, 
			String tnccsProtocolVersion, 
			TnccsWriter<TnccsBatch> writer, 
			TnccsReader<TnccsBatchContainer> reader,
			ImcHandlerBuilder imcHandlerBuilder,
			TnccsHandlerBuilder<TnccHandler> tnccHandlerBuilder,
			TnccsHandlerBuilder<TnccsValidationExceptionHandler> tnccsValExBuilder,
			TnccContentHandlerFactory tnccContentHandlerFactory,
			StateMachineBuilder<TnccContentHandler> stateMachineBuilder){
		
		this.tnccsProtocolId = tnccsProtocolId;
		this.tnccsProtocolVersion = tnccsProtocolVersion;
		
		this.reader = reader;
		this.writer = writer;
		
		this.imcHandlerBuilder = imcHandlerBuilder;
		this.tnccHandlerBuilder = tnccHandlerBuilder;
		this.tnccsValExBuilder = tnccsValExBuilder;
		this.tnccContentHandlerFactory = tnccContentHandlerFactory;
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
		
		ImcConnectionContext connectionContext = new DefaultImcConnectionContext(attributes,s);
		this.imcHandlerBuilder.setConnectionContext(connectionContext);
		ImcHandler imcHandler = this.imcHandlerBuilder.toHandler();
		
		this.tnccHandlerBuilder.setAttributes(attributes);
		TnccHandler tnccHandler = this.tnccHandlerBuilder.toHandler();
		
		this.tnccsValExBuilder.setAttributes(attributes);
		TnccsValidationExceptionHandler exceptionHandler = this.tnccsValExBuilder.toHandler();
		
		TnccContentHandler contentHandler = this.tnccContentHandlerFactory.createHandler(imcHandler, tnccHandler, exceptionHandler);
		
		StateHelper<TnccContentHandler> clientStateFactory = new DefaultClientStateFactory(attributes,contentHandler);
		this.stateMachineBuilder.setStateHelper(clientStateFactory);
		StateMachine machine = this.stateMachineBuilder.toStateMachine();
		
		// finalize session and run
		s.registerStatemachine(machine);
		s.registerConnection(connection);
		
		return s;
		
	}

}
