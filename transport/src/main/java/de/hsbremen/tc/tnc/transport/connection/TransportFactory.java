package de.hsbremen.tc.tnc.transport.connection;

import de.hsbremen.tc.tnc.transport.exception.ConnectionException;

public interface TransportFactory {

	public abstract TransportConnection connectTransporto(TransportAddress destination) throws ConnectionException;
}
