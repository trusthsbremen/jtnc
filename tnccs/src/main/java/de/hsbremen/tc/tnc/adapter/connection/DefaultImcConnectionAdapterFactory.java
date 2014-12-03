package de.hsbremen.tc.tnc.adapter.connection;

public class DefaultImcConnectionAdapterFactory implements ImcConnectionAdapterFactory{

	private final ImConnectionContext context;
	
	public DefaultImcConnectionAdapterFactory(ImConnectionContext context) {
		this.context = context;
	}

	@Override
	public ImcConnectionAdapter createConnection(long primaryId) {
		return new ImcConnectionAdapterIetfLong((int)primaryId, context);
	}

}
