package de.hsbremen.tc.tnc.session.context;

import de.hsbremen.tc.tnc.attribute.TncAttributeType;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.report.enums.ImHandshakeRetryReasonEnum;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessage;

public interface SessionConnectionContext {

	public void requestHandshakeRetry(ImHandshakeRetryReasonEnum reason) throws TncException;
	
	public void addMessage(TnccsMessage message) throws TncException;
	
	public Object getAttribute(TncAttributeType type) throws TncException;

}
