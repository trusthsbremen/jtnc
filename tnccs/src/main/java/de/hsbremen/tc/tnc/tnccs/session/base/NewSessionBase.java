package de.hsbremen.tc.tnc.tnccs.session.base;

import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.exception.ComprehensibleException;

public interface NewSessionBase {

	public abstract void start(boolean selfInitiated);
	
	public abstract void handle(ComprehensibleException e);
	
	public abstract void close();
	
	public abstract boolean isClosed();
	
	public abstract Attributed getAttributes();



	


}