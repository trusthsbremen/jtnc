package de.hsbremen.tc.tnc.im.adapter.tnccs;

import de.hsbremen.tc.tnc.im.manager.ImModuleManager;

interface TnccsAdapterBuilder<T,S> {

	public abstract T buildAdapter(ImModuleManager<S> manager);
}
