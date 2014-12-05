package de.hsbremen.tc.tnc.tnccs.im.manager;

import java.util.Set;

import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.report.SupportedMessageType;
import de.hsbremen.tc.tnc.tnccs.im.manager.exception.ImInitializeException;

public interface ImManager<T>{

	public abstract long add(T im) throws ImInitializeException;
	public abstract void remove(long id);
	public abstract long reserveAdditionalId(T im) throws TncException;
	public abstract void reportSupportedMessagesTypes(T im, Set<SupportedMessageType> types) throws TncException;
	
}
