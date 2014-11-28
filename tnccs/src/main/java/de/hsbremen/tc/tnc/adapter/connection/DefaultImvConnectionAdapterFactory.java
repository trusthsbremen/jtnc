package de.hsbremen.tc.tnc.adapter.connection;


public class DefaultImvConnectionAdapterFactory implements ImvConnectionAdapterFactory{

	private final ServerSessionConnectionContext context;
	
	public DefaultImvConnectionAdapterFactory(ServerSessionConnectionContext context) {
		this.context = context;
	}

	@Override
	public ImvConnectionAdapter createConnection(long primaryId) {
		return new ImvConnectionAdapterIetfLong((int)primaryId, context);
	}

}
