package de.hsbremen.tc.tnc.newp;

import java.util.Map;

import de.hsbremen.tc.tnc.adapter.im.ImAdapter;

public interface ImAdapterManager {
	public abstract Map<Long,? extends ImAdapter<?>> getAdapter();
	public abstract ImMessageRouter getRouter();
	public abstract void removeAdapter(long id);
}
