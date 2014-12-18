package de.hsbremen.tc.tnc.tnccs.session.base.simple;

import java.util.concurrent.Executors;

import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.message.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.message.tnccs.serialize.TnccsBatchContainer;
import de.hsbremen.tc.tnc.message.tnccs.serialize.bytebuffer.TnccsReader;
import de.hsbremen.tc.tnc.message.tnccs.serialize.bytebuffer.TnccsWriter;
import de.hsbremen.tc.tnc.tnccs.adapter.connection.ImcConnectionAdapterFactory;
import de.hsbremen.tc.tnc.tnccs.adapter.connection.ImcConnectionAdapterFactoryIetf;
import de.hsbremen.tc.tnc.tnccs.adapter.connection.ImcConnectionContext;
import de.hsbremen.tc.tnc.tnccs.adapter.connection.simple.DefaultImcConnectionContext;
import de.hsbremen.tc.tnc.tnccs.adapter.im.ImcAdapter;
import de.hsbremen.tc.tnc.tnccs.im.manager.ImAdapterManager;
import de.hsbremen.tc.tnc.tnccs.message.handler.ImcHandler;
import de.hsbremen.tc.tnc.tnccs.message.handler.TnccContentHandler;
import de.hsbremen.tc.tnc.tnccs.message.handler.TnccHandler;
import de.hsbremen.tc.tnc.tnccs.message.handler.TnccsValidationExceptionHandler;
import de.hsbremen.tc.tnc.tnccs.message.handler.simple.DefaultImcHandler;
import de.hsbremen.tc.tnc.tnccs.message.handler.simple.DefaultTnccContentHandler;
import de.hsbremen.tc.tnc.tnccs.message.handler.simple.DefaultTnccHandler;
import de.hsbremen.tc.tnc.tnccs.message.handler.simple.DefaultTnccsValidationExceptionHandler;
import de.hsbremen.tc.tnc.tnccs.session.base.AttributeCollection;
import de.hsbremen.tc.tnc.tnccs.session.base.Session;
import de.hsbremen.tc.tnc.tnccs.session.base.SessionFactory;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.StateHelper;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.StateMachine;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.simple.DefaultClientStateFactory;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.simple.DefaultClientStateMachine;
import de.hsbremen.tc.tnc.transport.TransportConnection;

public class DefaultClientSessionFactory implements SessionFactory {

	private final ImAdapterManager<ImcAdapter> adapterManager;

	private final String tnccsProtocolId;
	private final String tnccsProtocolVersion;
	
	private final TnccsWriter<TnccsBatch> writer;
	private final TnccsReader<TnccsBatchContainer> reader;
	
	public DefaultClientSessionFactory(
			String tnccsProtocolId, 
			String tnccsProtocolVersion, 
			ImAdapterManager<ImcAdapter> adapterManager, 
			TnccsWriter<TnccsBatch> writer, 
			TnccsReader<TnccsBatchContainer> reader){
		
		this.tnccsProtocolId = tnccsProtocolId;
		this.tnccsProtocolVersion = tnccsProtocolVersion;
		
		this.adapterManager = adapterManager;
		this.reader = reader;
		this.writer = writer;
	}
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.tnccs.session.base.SessionFactory#createTnccSession(de.hsbremen.tc.tnc.transport.connection.TransportConnection)
	 */
	@Override
	public Session createTnccsSession(TransportConnection connection){
		
		DefaultSession s = new DefaultSession(new DefaultSessionAttributes(this.tnccsProtocolId, this.tnccsProtocolVersion), this.writer, this.reader, Executors.newSingleThreadExecutor());
		
		AttributeCollection attributes = new AttributeCollection();
		attributes.add(s.getAttributes());
		
		Attributed connectionAttributes = connection.getAttributes();
		if(connectionAttributes != null){
			attributes.add(connectionAttributes);
		}
		
		ImcConnectionContext connectionContext = new DefaultImcConnectionContext(attributes,s);
		ImcConnectionAdapterFactory connectionFactory = new ImcConnectionAdapterFactoryIetf(connectionContext);
		
		ImcHandler imcHandler = new DefaultImcHandler(adapterManager,connectionFactory, connectionContext,adapterManager.getRouter());
		TnccHandler tnccHandler = new DefaultTnccHandler(attributes);
		TnccsValidationExceptionHandler exceptionHandler = new DefaultTnccsValidationExceptionHandler();
		
		TnccContentHandler contentHandler = new DefaultTnccContentHandler(imcHandler, tnccHandler, exceptionHandler);
		StateHelper<TnccContentHandler> clientStateFactory = new DefaultClientStateFactory(contentHandler);
		StateMachine machine = new DefaultClientStateMachine(clientStateFactory);
		
		// finalize session and run
		s.registerStatemachine(machine);
		s.registerConnection(connection);
		
		return s;
		
	}

}
