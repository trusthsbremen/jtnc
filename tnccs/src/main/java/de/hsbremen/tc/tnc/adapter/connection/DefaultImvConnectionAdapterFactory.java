package de.hsbremen.tc.tnc.adapter.connection;


public class DefaultImvConnectionAdapterFactory implements ImvConnectionAdapterFactory{

	private final ImvConnectionContext context;
	
	public DefaultImvConnectionAdapterFactory(ImvConnectionContext context) {
		this.context = context;
	}

	@Override
	public ImvConnectionAdapter createConnection(long primaryId) {
		return new ImvConnectionAdapterIetfLong((int)primaryId, context);
	}

}
