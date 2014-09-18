package de.hsbremen.tc.tnc.transport;

import de.hsbremen.tc.tnc.transport.connection.IfTAddress;
import de.hsbremen.tc.tnc.transport.connection.IfTConnection;
import de.hsbremen.tc.tnc.transport.exception.ConnectionException;

public interface IfTransportFactory {
	
	public IfTConnection connectTo(IfTAddress destination) throws ConnectionException;
}
