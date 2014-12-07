package de.hsbremen.tc.tnc.tnccs.adapter.connection;

public class ImcConnectionAdapterFactoryIetf implements ImcConnectionAdapterFactory{

	private final ImConnectionContext context;
	
	public ImcConnectionAdapterFactoryIetf(ImConnectionContext context) {
		this.context = context;
	}

	@Override
	public ImcConnectionAdapter createConnection(long primaryId) {
		return new ImcConnectionAdapterIetfLong((int)primaryId, context);
	}

}
