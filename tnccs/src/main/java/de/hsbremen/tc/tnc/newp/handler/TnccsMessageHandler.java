package de.hsbremen.tc.tnc.newp.handler;

import de.hsbremen.tc.tnc.connection.TncConnectionState;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageValue;

interface TnccsMessageHandler {

	// Maybe this method is needed to determine where to send the messages and where not, this could however also be filtered by the handler
	//public abstract Map<Long,Set<Long>> getForwardableMessageTypes();
	public abstract void setConnectionState(TncConnectionState imConnectionState);
	public abstract void requestMessages(TnccsSessionContext context);
	public abstract void forwardMessage(TnccsSessionContext context, TnccsMessageValue value);
}
