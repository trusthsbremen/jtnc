package de.hsbremen.tc.tnc.session.connection;

import de.hsbremen.tc.tnc.exception.ComprehensibleException;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsBatchContainer;

public interface BatchReceiver {

	public abstract void receive(TnccsBatchContainer bc);
	
	public abstract void handle(ComprehensibleException e);

}
