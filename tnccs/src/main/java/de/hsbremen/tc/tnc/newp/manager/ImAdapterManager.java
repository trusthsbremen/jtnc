package de.hsbremen.tc.tnc.newp.manager;

import java.util.Map;

import de.hsbremen.tc.tnc.adapter.im.ImAdapter;
import de.hsbremen.tc.tnc.newp.route.ImMessageRouter;

public interface ImAdapterManager<T extends ImAdapter<?>> {
	public abstract Map<Long,T> getAdapter();
	public abstract ImMessageRouter getRouter();
	public abstract void removeAdapter(long id);
}
