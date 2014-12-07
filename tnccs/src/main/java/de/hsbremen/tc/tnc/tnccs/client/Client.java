package de.hsbremen.tc.tnc.tnccs.client;

import de.hsbremen.tc.tnc.tnccs.client.enums.ConnectionChangeTypeEnum;
import de.hsbremen.tc.tnc.transport.connection.TransportConnection;

public interface Client {

	public abstract void notifyConnectionChange(TransportConnection connection, ConnectionChangeTypeEnum change);
	public abstract void notifyGlobalConnectionChange(ConnectionChangeTypeEnum change);

	public void terminate();
	
}
