package de.hsbremen.tc.tnc.newp.manager;

import java.util.Map;

public interface ImProxyDispenser<T> {
	
	public abstract Map<Long,T> getIm();
	public abstract void remove(long id);
	
}
