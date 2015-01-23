package de.hsbremen.tc.tnc.transport;

import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.transport.exception.ListenerClosedException;

public interface TnccsValueListener {
	
	public void receive(ByteBuffer b) throws ListenerClosedException;

	public void notifyClose();
	
}
