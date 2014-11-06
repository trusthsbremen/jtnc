package de.hsbremen.tc.tnc.im.adapter.connection;

import org.trustedcomputinggroup.tnc.ifimc.IMCConnection;

public interface ImcConnectionAdapterFactory {

	public abstract ImcConnectionAdapter createConnectionAdapter(
			IMCConnection connection);

}