package de.hsbremen.tc.tnc.tnccs.session.base.simple;

import de.hsbremen.tc.tnc.attribute.Attributed;
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
import de.hsbremen.tc.tnc.tnccs.session.connection.TnccsChannelFactory;
import de.hsbremen.tc.tnc.tnccs.session.connection.TnccsInputChannel;
import de.hsbremen.tc.tnc.tnccs.session.connection.TnccsOutputChannel;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.StateHelper;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.StateMachine;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.simple.DefaultServerStateFactory;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.simple.DefaultServerStateMachine;
import de.hsbremen.tc.tnc.transport.connection.TransportConnection;

public class DefaultServerSessionFactory implements SessionFactory{

	private final ImAdapterManager<ImvAdapter> adapterManager;
	private final TnccsChannelFactory channelFactory;
	
	public DefaultServerSessionFactory(ImAdapterManager<ImvAdapter> adapterManager, TnccsChannelFactory channelFactory){
		this.adapterManager = adapterManager;
		this.channelFactory = channelFactory;
	}
	
	public Session createTnccsSession(TransportConnection connection){
		
		DefaultSession s = new DefaultSession(new DefaultSessionAttributes(this.channelFactory.getProtocol(), this.channelFactory.getVersion()));
		
		TnccsOutputChannel outChannel = this.channelFactory.createOutputChannel(connection); 
		TnccsInputChannel inChannel = this.channelFactory.createInputChannel(connection, s);
		
		AttributeCollection attributes = new AttributeCollection();
		attributes.add(s.getAttributes());
		
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
		StateHelper<TncsContentHandler> serverStateFactory = new DefaultServerStateFactory(contentHandler);
		StateMachine machine = new DefaultServerStateMachine(serverStateFactory);
		
		// finalize session and run
		s.registerStatemachine(machine);
		s.registerInput(inChannel);
		s.registerOutput(outChannel);

		
		return s;
		
	}
	
	
	
}
