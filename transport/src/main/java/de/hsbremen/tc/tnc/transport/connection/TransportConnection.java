package de.hsbremen.tc.tnc.transport.connection;

import java.io.InputStream;
import java.io.OutputStream;

import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.attribute.TncAttributeType;
import de.hsbremen.tc.tnc.exception.SerializationException;
import de.hsbremen.tc.tnc.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.transport.exception.ConnectionAttributeNotFoundException;
import de.hsbremen.tc.tnc.transport.exception.ConnectionException;

public interface TransportConnection{

     public abstract TransportAddress getId(); 

	 public abstract boolean isSelfInititated();
	 
	 public abstract boolean isOpen();
	
	 public abstract void open() throws ConnectionException;
	    
	 public abstract void close();
	    
	 public abstract OutputStream getOutputStream();
	 
	 public abstract InputStream getInputStream();
	 
//	 public abstract void send(final TnccsBatch data) throws SerializationException, ConnectionException;
// 
//	 public abstract TnccsBatch receive() throws SerializationException, ConnectionException, ValidationException;
	 
	 public abstract Attributed getAttributes();
}
