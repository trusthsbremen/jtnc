package de.hsbremen.tc.tnc.tnccs.im.manager;

import java.util.Map;

import de.hsbremen.tc.tnc.tnccs.adapter.im.ImAdapter;
import de.hsbremen.tc.tnc.tnccs.im.route.ImMessageRouter;

public interface ImAdapterManager<T extends ImAdapter<?>> {
	public abstract Map<Long,T> getAdapter();
	public abstract ImMessageRouter getRouter();
	public abstract void removeAdapter(long id);
}
