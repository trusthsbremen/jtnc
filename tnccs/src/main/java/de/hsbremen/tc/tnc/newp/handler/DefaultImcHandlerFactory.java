package de.hsbremen.tc.tnc.newp.handler;

import de.hsbremen.tc.tnc.adapter.connection.ImcConnectionAdapterFactory;
import de.hsbremen.tc.tnc.adapter.im.ImcAdapter;
import de.hsbremen.tc.tnc.newp.manager.ImAdapterManager;

public class DefaultImcHandlerFactory implements ImcHandlerFactory{

	private final ImAdapterManager<ImcAdapter> imAdapter;
	
	public DefaultImcHandlerFactory(ImAdapterManager<ImcAdapter> imAdapter) {
		this.imAdapter = imAdapter;
	}

	@Override
	public ImcHandler getHandler(ImcConnectionAdapterFactory connectionFactory) {
		return new DefaultImcHandler(imAdapter, connectionFactory, this.imAdapter.getRouter());
		
	}

}
