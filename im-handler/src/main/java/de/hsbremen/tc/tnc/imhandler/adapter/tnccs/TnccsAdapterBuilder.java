package de.hsbremen.tc.tnc.imhandler.adapter.tnccs;

import de.hsbremen.tc.tnc.imhandler.manager.ImModuleManager;

interface TnccsAdapterBuilder<T,S> {

	public abstract T buildAdapter(ImModuleManager<S> manager);
}
