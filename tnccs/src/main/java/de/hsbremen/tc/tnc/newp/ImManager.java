package de.hsbremen.tc.tnc.newp;

import java.util.Set;

import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.report.SupportedMessageType;

public interface ImManager<T>{

	public abstract long add(T im) throws ImInitializeException;
	public abstract void remove(long id);
	public abstract long reserveAdditionalId(T im) throws TncException;
	public abstract void reportSupportedMessagesTypes(T im, Set<SupportedMessageType> types) throws TncException;
	
}
