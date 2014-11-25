package de.hsbremen.tc.tnc.client;

import de.hsbremen.tc.tnc.client.enums.TnccsConnectionChangeEventEnum;
import de.hsbremen.tc.tnc.client.exception.NoImIdsLeftException;
import de.hsbremen.tc.tnc.transport.connection.TransportConnection;
import de.hsbremen.tc.tnc.transport.exception.ConnectionException;

public interface TnccsConnector {
	
	public abstract void initialize() throws NoImIdsLeftException;
	
	public abstract void terminate();

//	public abstract void notifyGlobalConnectionChange() throws ConnectionException;
	
	public abstract void notifyConnectionChange(TnccsConnectionChangeEventEnum event, TransportConnection connection) throws ConnectionException;
	
//	public abstract void addPeer(String peerId, IfTAddress address);
//	
//	public abstract void removePeer(String peerId);
}
