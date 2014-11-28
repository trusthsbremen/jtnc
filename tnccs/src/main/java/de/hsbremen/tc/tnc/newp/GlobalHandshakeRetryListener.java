package de.hsbremen.tc.tnc.newp;

import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.report.enums.ImHandshakeRetryReasonEnum;

public interface GlobalHandshakeRetryListener {

	public void requestGlobalHandshakeRetry(ImHandshakeRetryReasonEnum reason) throws TncException;
}