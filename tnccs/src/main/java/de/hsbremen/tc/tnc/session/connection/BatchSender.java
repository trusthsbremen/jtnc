package de.hsbremen.tc.tnc.session.connection;

import de.hsbremen.tc.tnc.exception.ComprehensibleException;
import de.hsbremen.tc.tnc.tnccs.batch.TnccsBatch;

public interface BatchSender {

	public void register(TnccsOutputChannel channel);

	public void send(TnccsBatch batch) throws ComprehensibleException;
	
	public void close();

}
