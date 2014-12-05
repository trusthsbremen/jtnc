package de.hsbremen.tc.tnc.tnccs.adapter.connection;


public interface ImcConnectionAdapterFactory {

	public abstract ImcConnectionAdapter createConnection(long primaryId);

}
