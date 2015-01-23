package de.hsbremen.tc.tnc.tnccs.session.base.simple;

import java.util.concurrent.Executors;

import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.message.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.message.tnccs.serialize.TnccsBatchContainer;
import de.hsbremen.tc.tnc.message.tnccs.serialize.bytebuffer.TnccsReader;
import de.hsbremen.tc.tnc.message.tnccs.serialize.bytebuffer.TnccsWriter;
import de.hsbremen.tc.tnc.tnccs.adapter.connection.ImvConnectionAdapterFactory;
import de.hsbremen.tc.tnc.tnccs.adapter.connection.ImvConnectionAdapterFactoryIetf;
import de.hsbremen.tc.tnc.tnccs.adapter.connection.ImvConnectionContext;
import de.hsbremen.tc.tnc.tnccs.adapter.connection.simple.DefaultImvConnectionContext;
import de.hsbremen.tc.tnc.tnccs.adapter.im.ImvAdapter;
import de.hsbremen.tc.tnc.tnccs.im.manager.ImAdapterManager;
import de.hsbremen.tc.tnc.tnccs.message.handler.ImvHandler;
import de.hsbremen.tc.tnc.tnccs.message.handler.TnccsValidationExceptionHandler;
import de.hsbremen.tc.tnc.tnccs.message.handler.TncsContentHandler;
import de.hsbremen.tc.tnc.tnccs.message.handler.TncsHandler;
import de.hsbremen.tc.tnc.tnccs.message.handler.simple.DefaultImvHandler;
import de.hsbremen.tc.tnc.tnccs.message.handler.simple.DefaultTnccsValidationExceptionHandler;
import de.hsbremen.tc.tnc.tnccs.message.handler.simple.DefaultTncsContentHandler;
import de.hsbremen.tc.tnc.tnccs.message.handler.simple.DefaultTncsHandler;
import de.hsbremen.tc.tnc.tnccs.session.base.AttributeCollection;
import de.hsbremen.tc.tnc.tnccs.session.base.Session;
import de.hsbremen.tc.tnc.tnccs.session.base.SessionFactory;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.StateHelper;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.StateMachine;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.simple.DefaultServerStateFactory;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.simple.DefaultServerStateMachine;
import de.hsbremen.tc.tnc.transport.TransportConnection;

public class DefaultServerSessionFactory implements SessionFactory {

	private final ImAdapterManager<ImvAdapter> adapterManager;

	private final String tnccsProtocolId;
	private final String tnccsProtocolVersion;
	
	private final TnccsWriter<TnccsBatch> writer;
	private final TnccsReader<TnccsBatchContainer> reader;
	
	public DefaultServerSessionFactory(
			String tnccsProtocolId, 
			String tnccsProtocolVersion, 
			ImAdapterManager<ImvAdapter> adapterManager, 
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
		Attributed sessionAttributes = s.getAttributes();
		if(sessionAttributes != null){
			attributes.add(s.getAttributes());
		}
		
		Attributed connectionAttributes = connection.getAttributes();
		if(connectionAttributes != null){
			attributes.add(connectionAttributes);
		}
		
		ImvConnectionContext connectionContext = new DefaultImvConnectionContext(attributes,s);
		ImvConnectionAdapterFactory connectionFactory = new ImvConnectionAdapterFactoryIetf(connectionContext);
		
		ImvHandler imvHandler = new DefaultImvHandler(adapterManager,connectionFactory, connectionContext,adapterManager.getRouter());
		TncsHandler tncsHandler = new DefaultTncsHandler(attributes);
		TnccsValidationExceptionHandler exceptionHandler = new DefaultTnccsValidationExceptionHandler();
		
		TncsContentHandler contentHandler = new DefaultTncsContentHandler(imvHandler, tncsHandler, exceptionHandler);
		
		StateHelper<TncsContentHandler> serverStateFactory = new DefaultServerStateFactory(attributes,contentHandler);
		StateMachine machine = new DefaultServerStateMachine(serverStateFactory);
		
		// finalize session and run
		s.registerStatemachine(machine);
		s.registerConnection(connection);
		
		return s;
		
	}

}
