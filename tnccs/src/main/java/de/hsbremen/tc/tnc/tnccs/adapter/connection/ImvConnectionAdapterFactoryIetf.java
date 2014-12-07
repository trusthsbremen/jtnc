package de.hsbremen.tc.tnc.tnccs.adapter.connection;


public class ImvConnectionAdapterFactoryIetf implements ImvConnectionAdapterFactory{

	private final ImvConnectionContext context;
	
	public ImvConnectionAdapterFactoryIetf(ImvConnectionContext context) {
		this.context = context;
	}

	@Override
	public ImvConnectionAdapter createConnection(long primaryId) {
		return new ImvConnectionAdapterIetfLong((int)primaryId, context);
	}

}
