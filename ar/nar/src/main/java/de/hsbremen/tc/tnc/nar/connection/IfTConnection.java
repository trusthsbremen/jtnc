package de.hsbremen.tc.tnc.nar.connection;

import de.hsbremen.tc.tnc.tnccs.batch.TnccsBatch;

public interface IfTConnection {

     public abstract String getId(); 
	
	 public abstract boolean isSelfInititated();
	 
	 public abstract boolean isOpen();
	
	 public abstract void open();
	    
	 public abstract void close();
	    
	 public abstract void send(TnccsBatch data);
 
	 public abstract TnccsBatch receive();
}
