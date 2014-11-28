package de.hsbremen.tc.tnc.newp;

import de.hsbremen.tc.tnc.adapter.connection.ImvConnectionAdapterFactory;

public interface ImvHandlerFactory {

	public abstract ImvHandler getHandler(ImvConnectionAdapterFactory connectionFactory);

}
