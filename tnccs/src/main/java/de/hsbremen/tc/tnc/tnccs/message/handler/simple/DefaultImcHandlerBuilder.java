package de.hsbremen.tc.tnc.tnccs.message.handler.simple;

import de.hsbremen.tc.tnc.tnccs.adapter.connection.ImcConnectionAdapterFactory;
import de.hsbremen.tc.tnc.tnccs.adapter.connection.ImcConnectionAdapterFactoryIetf;
import de.hsbremen.tc.tnc.tnccs.adapter.connection.ImcConnectionContext;
import de.hsbremen.tc.tnc.tnccs.adapter.connection.simple.DefaultImcConnectionContext;
import de.hsbremen.tc.tnc.tnccs.adapter.im.ImcAdapter;
import de.hsbremen.tc.tnc.tnccs.im.manager.ImAdapterManager;
import de.hsbremen.tc.tnc.tnccs.message.handler.ImcHandler;
import de.hsbremen.tc.tnc.tnccs.message.handler.ImcHandlerBuilder;
import de.hsbremen.tc.tnc.tnccs.session.base.AttributeCollection;

public class DefaultImcHandlerBuilder implements ImcHandlerBuilder {

	private final ImAdapterManager<ImcAdapter> adapterManager;
	private ImcConnectionContext connectionContext;
	public DefaultImcHandlerBuilder(ImAdapterManager<ImcAdapter> adapterManager){
		this.adapterManager = adapterManager;
		this.connectionContext = new DefaultImcConnectionContext(new AttributeCollection(),null);
	}
	
	@Override
	public ImcHandlerBuilder setConnectionContext(ImcConnectionContext connectionContext){
		if(connectionContext != null){
			this.connectionContext = connectionContext;
		}
		
		return this;
	}

	@Override
	public ImcHandler toHandler(){
		
		ImcConnectionAdapterFactory connectionFactory = new ImcConnectionAdapterFactoryIetf(connectionContext);
		ImcHandler imvHandler = new DefaultImcHandler(adapterManager,connectionFactory, connectionContext,adapterManager.getRouter());
		
		// clear side effects
		this.connectionContext = new DefaultImcConnectionContext(new AttributeCollection(),null);
		
		return imvHandler;
	}
	
}
