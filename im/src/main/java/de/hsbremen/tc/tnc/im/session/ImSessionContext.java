package de.hsbremen.tc.tnc.im.session;

import de.hsbremen.tc.tnc.attribute.TncAttributeType;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.im.adapter.ImConnectionStateEnum;
import de.hsbremen.tc.tnc.im.adapter.ImHandshakeRetryReasonEnum;

public interface ImSessionContext {

	public abstract ImConnectionStateEnum getConnectionState();

	public abstract Object getAttribute(TncAttributeType type) throws TncException;
	
	public abstract void requestConnectionHandshakeRetry(ImHandshakeRetryReasonEnum reason);
}
