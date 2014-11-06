package de.hsbremen.tc.tnc.im.module;

import java.util.List;

public interface ImModule /*extends Attributed*/{


	public abstract long getPrimaryId();
	
	public abstract void addImId(long imId);
	
	public abstract List<Long> getAllImIds();
	
	public abstract void setSupportedMessageTypes(
			List<SupportedMessageType> supportedTypes);
	
	public abstract List<SupportedMessageType> getSupportedMessageTypes();

	public abstract boolean supportsTncsFirst();

	public void setSupportsTncsFirst(boolean support);
	
}