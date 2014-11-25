package de.hsbremen.tc.tnc.adapter.connection;


public interface ImcConnectionAdapterFactory {

	public abstract ImcConnectionAdapter createConnection(long primaryId);

}
