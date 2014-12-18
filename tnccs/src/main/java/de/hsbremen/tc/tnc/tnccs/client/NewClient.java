package de.hsbremen.tc.tnc.tnccs.client;

import de.hsbremen.tc.tnc.tnccs.client.enums.ConnectionChangeTypeEnum;
import de.hsbremen.tc.tnc.transport.newp.connection.TransportConnection;

public interface NewClient {

	public abstract void notifyConnectionChange(TransportConnection connection, ConnectionChangeTypeEnum change);
	public abstract void notifyGlobalConnectionChange(ConnectionChangeTypeEnum change);

	public void start();
	public void stop();
	
}
