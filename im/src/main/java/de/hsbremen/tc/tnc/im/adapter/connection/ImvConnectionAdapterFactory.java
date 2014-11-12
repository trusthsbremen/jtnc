package de.hsbremen.tc.tnc.im.adapter.connection;

import org.trustedcomputinggroup.tnc.ifimv.IMVConnection;

public interface ImvConnectionAdapterFactory {

	public abstract ImvConnectionAdapter createConnectionAdapter(
			IMVConnection connection);

}