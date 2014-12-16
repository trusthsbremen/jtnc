package de.hsbremen.tc.tnc.transport.newp.connection;

import de.hsbremen.tc.tnc.message.util.ByteBuffer;

public interface TnccsValueListener {
	
	public void receive(ByteBuffer b) throws ListenerClosedException;
	
}
