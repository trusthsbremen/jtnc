package de.hsbremen.tc.tnc.attribute;

import de.hsbremen.tc.tnc.exception.TncException;

public interface Attributed {

	public abstract Object getAttribute(TncAttributeType type) throws TncException;
	public abstract void setAttribute(TncAttributeType type, Object value) throws TncException;
	
}
