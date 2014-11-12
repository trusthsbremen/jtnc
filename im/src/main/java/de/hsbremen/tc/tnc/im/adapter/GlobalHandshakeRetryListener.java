package de.hsbremen.tc.tnc.im.adapter;

import de.hsbremen.tc.tnc.exception.TncException;

public interface GlobalHandshakeRetryListener {

	public void requestGlobalHandshakeRetry(ImHandshakeRetryReasonEnum reason) throws TncException;
}
