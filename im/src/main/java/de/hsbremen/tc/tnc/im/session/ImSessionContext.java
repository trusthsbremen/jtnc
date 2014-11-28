package de.hsbremen.tc.tnc.im.session;

import de.hsbremen.tc.tnc.attribute.TncAttributeType;
import de.hsbremen.tc.tnc.connection.TncConnectionState;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.report.enums.ImHandshakeRetryReasonEnum;

public interface ImSessionContext {

	public abstract TncConnectionState getConnectionState();

	public abstract Object getAttribute(TncAttributeType type) throws TncException;
	
	public abstract void requestConnectionHandshakeRetry(ImHandshakeRetryReasonEnum reason);

	
}
