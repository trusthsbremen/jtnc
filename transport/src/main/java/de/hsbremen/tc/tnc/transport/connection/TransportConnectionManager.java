package de.hsbremen.tc.tnc.transport.connection;

public interface TransportConnectionManager {

	public abstract TransportConnection findConnection(TransportAddress address);

}
