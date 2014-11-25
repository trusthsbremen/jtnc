package org.ietf.nea.pt.connection;

import java.util.HashMap;
import java.util.Map;

import de.hsbremen.tc.tnc.transport.connection.TransportAddress;
import de.hsbremen.tc.tnc.transport.connection.TransportConnection;
import de.hsbremen.tc.tnc.transport.connection.TransportConnectionManager;

public abstract class AbstractTransportConnectionManager implements TransportConnectionManager{

	private final Map<TransportAddress, TransportConnection> connections;
	
	public AbstractTransportConnectionManager(){
		this.connections = new HashMap<>();
	}
	
	@Override
	public TransportConnection findConnection(TransportAddress address) {
		if(connections.containsKey(address) ){
			TransportConnection connection = connections.get(address);
			if(connection.isOpen()){
				return connection;
			}
		}
		
		return this.connect(address);
	}
	
	protected abstract TransportConnection connect(TransportAddress address);
}
