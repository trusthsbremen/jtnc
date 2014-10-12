package de.hsbremen.tc.tnc.im;

import java.util.Map;

import de.hsbremen.tc.tnc.exception.AttributeException;

public interface Attributed {

	 public abstract Object getAttribute(final Long id) throws AttributeException;
	 
	 public abstract Map<Long,Object> getAttributes();

	 public abstract void setAttribute(final Long id, Object value) throws AttributeException;
}
