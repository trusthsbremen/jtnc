package de.hsbremen.tc.tnc.tnccs.session.connection;

import de.hsbremen.tc.tnc.exception.ComprehensibleException;
import de.hsbremen.tc.tnc.message.tnccs.serialize.TnccsBatchContainer;

public interface TnccsInputChannelListener {

	public abstract void receive(TnccsBatchContainer bc) throws ListenerClosedException;
	
	public abstract void handle(ComprehensibleException e);

}
