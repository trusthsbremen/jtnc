package de.hsbremen.tc.tnc.transport.connection;

import de.hsbremen.tc.tnc.exception.SerializationException;
import de.hsbremen.tc.tnc.exception.ValidationException;
import de.hsbremen.tc.tnc.im.Attributed;
import de.hsbremen.tc.tnc.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.transport.exception.ConnectionException;

public interface IfTConnection extends Attributed{

     public abstract String getId(); 

	 //public abstract boolean isSelfInititated();
	 
	 public abstract boolean isOpen();
	
	 public abstract void open() throws ConnectionException;
	    
	 public abstract void close();
	    
	 public abstract void send(final TnccsBatch data) throws SerializationException, ConnectionException;
 
	 public abstract TnccsBatch receive() throws SerializationException, ConnectionException, ValidationException;
}
