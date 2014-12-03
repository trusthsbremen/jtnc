package de.hsbremen.tc.tnc.adapter.connection;

import java.util.List;

import de.hsbremen.tc.tnc.attribute.TncAttributeType;
import de.hsbremen.tc.tnc.connection.TncConnectionState;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.report.enums.ImHandshakeRetryReasonEnum;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessage;

public interface ImConnectionContext{
	
	public void addMessage(TnccsMessage message) throws TncException;
	
	public List<TnccsMessage> clearMessage();
	
	public void reportConnectionState(TncConnectionState connectionState);
	
	public TncConnectionState getReportedConnectionState();
	
	public void requestHandshakeRetry(ImHandshakeRetryReasonEnum reason) throws TncException ;
	
	public Object getAttribute(TncAttributeType type) throws TncException;
	
	public void setAttribute(TncAttributeType type, Object value) throws TncException;

	public void invalidate();
	
}
