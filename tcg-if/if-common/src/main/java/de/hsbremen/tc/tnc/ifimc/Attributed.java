package de.hsbremen.tc.tnc.ifimc;

import java.util.Map;

public interface Attributed {

	 public abstract Object getAttribute(final Long id);
	 
	 public abstract Map<Long,Object> getAttributes();

	 public abstract boolean setAttribute(final Long id, Object value);
}
