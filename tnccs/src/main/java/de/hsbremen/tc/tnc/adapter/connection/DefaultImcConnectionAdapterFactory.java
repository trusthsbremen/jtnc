package de.hsbremen.tc.tnc.adapter.connection;

import de.hsbremen.tc.tnc.session.context.SessionConnectionContext;

public class DefaultImcConnectionAdapterFactory implements ImcConnectionAdapterFactory{

	private final SessionConnectionContext context;
	
	public DefaultImcConnectionAdapterFactory(SessionConnectionContext context) {
		this.context = context;
	}

	@Override
	public ImcConnectionAdapter createConnection(long primaryId) {
		return new ImcConnectionAdapterIetfLong((int)primaryId, context);
	}

}
