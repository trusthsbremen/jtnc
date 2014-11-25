package de.hsbremen.tc.tnc.newp;

import de.hsbremen.tc.tnc.adapter.connection.ImcConnectionAdapterFactory;

public interface ImHandlerFactory {

	public abstract ImHandler getHandler(ImcConnectionAdapterFactory connectionFactory);
}
