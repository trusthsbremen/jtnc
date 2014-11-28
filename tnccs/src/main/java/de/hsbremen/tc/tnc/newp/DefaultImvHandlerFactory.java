package de.hsbremen.tc.tnc.newp;

import de.hsbremen.tc.tnc.adapter.connection.ImvConnectionAdapterFactory;
import de.hsbremen.tc.tnc.adapter.im.ImvAdapter;

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
