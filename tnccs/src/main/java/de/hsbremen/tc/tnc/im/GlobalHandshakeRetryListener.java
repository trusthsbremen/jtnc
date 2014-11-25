package de.hsbremen.tc.tnc.im;

import de.hsbremen.tc.tnc.connection.ImHandshakeRetryReasonEnum;
import de.hsbremen.tc.tnc.exception.TncException;

public interface GlobalHandshakeRetryListener {

	public void requestGlobalHandshakeRetry(ImHandshakeRetryReasonEnum reason) throws TncException;
}
