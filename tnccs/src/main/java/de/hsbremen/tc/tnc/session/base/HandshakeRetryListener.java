package de.hsbremen.tc.tnc.session.base;

import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.report.enums.ImHandshakeRetryReasonEnum;

public interface HandshakeRetryListener {

	public abstract void retryHandshake(ImHandshakeRetryReasonEnum reason) throws TncException;

}
