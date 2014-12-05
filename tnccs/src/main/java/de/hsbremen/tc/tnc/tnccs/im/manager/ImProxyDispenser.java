package de.hsbremen.tc.tnc.tnccs.im.manager;

import java.util.Map;

public interface ImProxyDispenser<T> {
	
	public abstract Map<Long,T> getIm();
	public abstract void remove(long id);
	
}
