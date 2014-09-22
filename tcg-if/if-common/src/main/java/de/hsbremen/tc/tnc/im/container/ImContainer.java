package de.hsbremen.tc.tnc.im.container;

import java.util.List;

import de.hsbremen.tc.tnc.im.Attributed;

public interface ImContainer<T> extends Attributed{

	public abstract T getIm();

	public abstract long getPrimaryId();
	
	public abstract void addImId(long imId);
	
	public abstract List<Long> getAllImIds();
	
	public abstract void setSupportedMessageTypes(
			List<SupportedMessageType> supportedTypes);
	
	public abstract List<SupportedMessageType> getSupportedMessageTypes();

	public abstract boolean supportsTncsFirst();
}