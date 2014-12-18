package de.hsbremen.tc.tnc.transport;

import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.transport.exception.ConnectionException;

public interface TransportConnection{

     public abstract TransportAddress getAddress(); 

	 public abstract boolean isSelfInititated();
	 
	 public abstract boolean isOpen();
	
	 public abstract void open(TnccsValueListener listener) throws ConnectionException;
	 
	 public abstract void send(ByteBuffer buffer) throws ConnectionException;
	    
	 public abstract void close();

	 public abstract Attributed getAttributes();
}
