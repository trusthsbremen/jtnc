package de.hsbremen.tc.tnc.session.connection;

import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.exception.SerializationException;
import de.hsbremen.tc.tnc.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.transport.exception.ConnectionException;

public interface TnccsConnection extends Runnable{

	public abstract boolean isSelfInitiated();

	public abstract void send(TnccsBatch b) throws ConnectionException,
			SerializationException;
	
	public abstract Attributed getAttributes();

}