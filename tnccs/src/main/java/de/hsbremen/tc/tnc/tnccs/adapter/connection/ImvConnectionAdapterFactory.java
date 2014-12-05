package de.hsbremen.tc.tnc.tnccs.adapter.connection;


public interface ImvConnectionAdapterFactory{

	public abstract ImvConnectionAdapter createConnection(long primaryId);

}
