package de.hsbremen.tc.tnc.newp;

import de.hsbremen.tc.tnc.adapter.connection.ImcConnectionAdapterFactory;

public interface ImcHandlerFactory {

	public abstract ImcHandler getHandler(ImcConnectionAdapterFactory connectionFactory);
}
