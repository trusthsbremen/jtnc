package de.hsbremen.tc.tnc.session.connection;

import de.hsbremen.tc.tnc.transport.connection.TransportConnection;

public interface TnccsConnectionFactory {

	public abstract TnccsConnection createConnection(
			TransportConnection connection);

}