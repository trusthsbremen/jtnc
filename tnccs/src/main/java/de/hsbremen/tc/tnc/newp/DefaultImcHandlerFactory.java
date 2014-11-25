package de.hsbremen.tc.tnc.newp;

import de.hsbremen.tc.tnc.adapter.connection.ImcConnectionAdapterFactory;

public class DefaultImcHandlerFactory implements ImHandlerFactory{

	private final ImAdapterManager imAdapter;
	
	public DefaultImcHandlerFactory(ImAdapterManager imAdapter) {
		this.imAdapter = imAdapter;
	}

	@Override
	public ImHandler getHandler(ImcConnectionAdapterFactory connectionFactory) {
		
		return new DefaultImcHandler(imAdapter, connectionFactory, this.imAdapter.getRouter());
		
	}

}
