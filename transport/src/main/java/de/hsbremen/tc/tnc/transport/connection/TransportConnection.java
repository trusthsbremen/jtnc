package de.hsbremen.tc.tnc.transport.connection;

import java.io.InputStream;
import java.io.OutputStream;

import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.transport.exception.ConnectionException;

public interface TransportConnection{

     public abstract TransportAddress getAddress(); 

	 public abstract boolean isSelfInititated();
	 
	 public abstract boolean isOpen();
	
	 public abstract void open() throws ConnectionException;
	    
	 public abstract void close();
	    
	 public abstract OutputStream getOutputStream() throws ConnectionException;
	 
	 public abstract InputStream getInputStream() throws ConnectionException;
	 
//	 public abstract void send(final TnccsBatch data) throws SerializationException, ConnectionException;
// 
//	 public abstract TnccsBatch receive() throws SerializationException, ConnectionException, ValidationException;
	 
	 public abstract Attributed getAttributes();
}
