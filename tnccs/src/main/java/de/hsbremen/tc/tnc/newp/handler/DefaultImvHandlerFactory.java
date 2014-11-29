package de.hsbremen.tc.tnc.newp.handler;

import de.hsbremen.tc.tnc.adapter.connection.ImvConnectionAdapterFactory;
import de.hsbremen.tc.tnc.adapter.im.ImvAdapter;
import de.hsbremen.tc.tnc.newp.manager.ImAdapterManager;

public class DefaultImvHandlerFactory implements ImvHandlerFactory{

	private final ImAdapterManager<ImvAdapter> imAdapter;
	
	public DefaultImvHandlerFactory(ImAdapterManager<ImvAdapter> imAdapter) {
		this.imAdapter = imAdapter;
	}

	@Override
	public ImvHandler getHandler(ImvConnectionAdapterFactory connectionFactory) {
		return new DefaultImvHandler(imAdapter, connectionFactory, this.imAdapter.getRouter());
		
	}

}
