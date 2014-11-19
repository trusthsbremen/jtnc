package de.hsbremen.tc.tnc.imhandler.module;

import de.hsbremen.tc.tnc.im.module.ImModule;

public interface TnccsImModuleHolder<T> extends ImModule {

	public abstract T getIm();
	
}
