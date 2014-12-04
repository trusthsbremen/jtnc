package de.hsbremen.tc.tnc.newp.handler;

import java.util.List;

import de.hsbremen.tc.tnc.connection.TncConnectionState;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessage;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageValue;

interface TnccsMessageHandler {

	// Maybe this method is needed to determine where to send the messages and where not, this could however also be filtered by the handler
	//public abstract Map<Long,Set<Long>> getForwardableMessageTypes();
	public abstract void setConnectionState(TncConnectionState state);
	public abstract List<TnccsMessage> requestMessages();
	public abstract List<TnccsMessage> forwardMessage(TnccsMessageValue value);
}
