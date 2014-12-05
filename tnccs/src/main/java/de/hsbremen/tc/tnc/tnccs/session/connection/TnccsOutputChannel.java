package de.hsbremen.tc.tnc.tnccs.session.connection;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.transport.exception.ConnectionException;

public interface TnccsOutputChannel{
	
	public void send(TnccsBatch batch) throws ConnectionException, SerializationException;

	public void close();
	
}
