package de.hsbremen.tc.tnc.im.adapter.tncc;

import java.util.Set;

import org.trustedcomputinggroup.tnc.ifimc.TNCException;

import de.hsbremen.tc.tnc.im.adapter.GlobalHandshakeRetryListener;
import de.hsbremen.tc.tnc.im.module.SupportedMessageType;

public interface TnccAdapter {
	
	public abstract GlobalHandshakeRetryListener getHandshakeRetryListener();
	
	public abstract void reportMessageTypes(
			Set<SupportedMessageType> supportedTypes) throws TNCException;

	public abstract long reserveAdditionalId() throws TNCException;

}