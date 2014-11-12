package de.hsbremen.tc.tnc.im.adapter;

import java.util.Set;

import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.im.adapter.GlobalHandshakeRetryListener;
import de.hsbremen.tc.tnc.im.module.SupportedMessageType;

public interface TnccsAdapter {
	
	public abstract GlobalHandshakeRetryListener getHandshakeRetryListener();
	
	public abstract void reportMessageTypes(
			Set<SupportedMessageType> supportedTypes) throws TncException;

	public abstract long reserveAdditionalId() throws TncException;

}