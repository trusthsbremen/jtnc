package de.hsbremen.tc.tnc.session.context;

import de.hsbremen.tc.tnc.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.tnccs.exception.HandlingException;
import de.hsbremen.tc.tnc.tnccs.exception.SerializationException;
import de.hsbremen.tc.tnc.transport.exception.ConnectionException;

public interface SessionContext extends TncSession {
	
	abstract boolean isServerSession();
	
	abstract TnccsBatch getCurrentBatch();
	
	// TODO throws exception if not working
	abstract TnccsBatch receiveBatch() throws SerializationException, ConnectionException;

	// TODO throws exception if not working
	abstract void sendBatch(TnccsBatch b) throws SerializationException, ConnectionException;
	
	abstract void handleBatch(TnccsBatch b) throws HandlingException;
	
	abstract void closeSession();
}
