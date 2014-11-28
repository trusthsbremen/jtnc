package de.hsbremen.tc.tnc.adapter.connection;


public interface ImvConnectionAdapterFactory{

	public abstract ImvConnectionAdapter createConnection(long primaryId);

}
