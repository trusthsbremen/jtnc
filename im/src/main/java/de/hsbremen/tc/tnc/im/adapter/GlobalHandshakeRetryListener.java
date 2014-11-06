package de.hsbremen.tc.tnc.im.adapter;

import org.trustedcomputinggroup.tnc.ifimc.TNCException;

public interface GlobalHandshakeRetryListener {

	public void requestGlobalHandshakeRetry(ImHandshakeRetryReasonEnum reason) throws TNCException;
}
