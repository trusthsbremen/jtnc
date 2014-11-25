package de.hsbremen.tc.tnc.imhandler.module;

import de.hsbremen.tc.tnc.im.Im;

public interface TnccsImModuleHolder<T> extends Im {

	public abstract T getIm();
	
}
