package de.hsbremen.tc.tnc.tnccs.message.handler.simple;

import de.hsbremen.tc.tnc.tnccs.adapter.connection.ImvConnectionAdapterFactory;
import de.hsbremen.tc.tnc.tnccs.adapter.connection.ImvConnectionAdapterFactoryIetf;
import de.hsbremen.tc.tnc.tnccs.adapter.connection.ImvConnectionContext;
import de.hsbremen.tc.tnc.tnccs.adapter.connection.simple.DefaultImvConnectionContext;
import de.hsbremen.tc.tnc.tnccs.adapter.im.ImvAdapter;
import de.hsbremen.tc.tnc.tnccs.im.manager.ImAdapterManager;
import de.hsbremen.tc.tnc.tnccs.message.handler.ImvHandler;
import de.hsbremen.tc.tnc.tnccs.message.handler.ImvHandlerBuilder;
import de.hsbremen.tc.tnc.tnccs.session.base.AttributeCollection;

public class DefaultImvHandlerBuilder implements ImvHandlerBuilder {

	private final ImAdapterManager<ImvAdapter> adapterManager;
	private ImvConnectionContext connectionContext;
	public DefaultImvHandlerBuilder(ImAdapterManager<ImvAdapter> adapterManager){
		this.adapterManager = adapterManager;
		this.connectionContext = new DefaultImvConnectionContext(new AttributeCollection(),null);
	}

	@Override
	public ImvHandlerBuilder setConnectionContext(ImvConnectionContext connectionContext){
		if(connectionContext != null){
			this.connectionContext = connectionContext;
		}
		
		return this;
	}
	
	@Override
	public ImvHandler toHandler(){
		
		ImvConnectionAdapterFactory connectionFactory = new ImvConnectionAdapterFactoryIetf(connectionContext);
		ImvHandler imvHandler = new DefaultImvHandler(adapterManager,connectionFactory, connectionContext,adapterManager.getRouter());
		
		// clear side effects
		this.connectionContext = new DefaultImvConnectionContext(new AttributeCollection(),null);
		
		return imvHandler;
	}
	
}
