package de.hsbremen.tc.tnc.newp.handler;

import de.hsbremen.tc.tnc.connection.TncConnectionState;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessage;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageValue;

public interface TnccsHandler {

	// Maybe this method is needed to determine where to send the messages and where not, this could however also be filtered by the handler
	//public abstract Map<Long,Set<Long>> getForwardableMessageTypes();
	public abstract void setConnectionState(TncConnectionState state);
	public abstract TnccsMessage requestMessages();
	public abstract TnccsMessage forwardMessage(TnccsMessageValue value);
	public abstract TncConnectionState getAccessDecision();
	
}
