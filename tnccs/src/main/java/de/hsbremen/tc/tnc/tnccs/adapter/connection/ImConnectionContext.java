package de.hsbremen.tc.tnc.tnccs.adapter.connection;

import java.util.List;

import de.hsbremen.tc.tnc.attribute.TncAttributeType;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessage;
import de.hsbremen.tc.tnc.report.enums.ImHandshakeRetryReasonEnum;

public interface ImConnectionContext{
	
	public void addMessage(TnccsMessage message) throws TncException;
	
	public List<TnccsMessage> clearMessage();
	
	public void requestHandshakeRetry(ImHandshakeRetryReasonEnum reason) throws TncException ;
	
	public Object getAttribute(TncAttributeType type) throws TncException;
	
	public void setAttribute(TncAttributeType type, Object value) throws TncException;

	public void invalidate();
	
}
