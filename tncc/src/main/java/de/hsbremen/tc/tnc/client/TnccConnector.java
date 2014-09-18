package de.hsbremen.tc.tnc.client;

import java.util.List;

import org.trustedcomputinggroup.tnc.ifimc.IMC;

import de.hsbremen.tc.tnc.client.exception.NoImIdsLeftException;
import de.hsbremen.tc.tnc.transport.connection.IfTAddress;
import de.hsbremen.tc.tnc.transport.exception.ConnectionException;

public interface TnccConnector {
	
	public abstract void startUp() throws NoImIdsLeftException;
	
	public abstract void shutdown();

	public abstract void requestHandshakeWithAll() throws ConnectionException;
	
	public abstract void requestHandshakeWith(String peerId) throws ConnectionException;
	
	public abstract void addPeer(String peerId, IfTAddress address);
	
	public abstract void removePeer(String peerId);
}
