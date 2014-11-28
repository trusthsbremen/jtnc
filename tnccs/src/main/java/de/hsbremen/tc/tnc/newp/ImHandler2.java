package de.hsbremen.tc.tnc.newp;

import de.hsbremen.tc.tnc.connection.TncConnectionState;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageValue;

public interface ImHandler2 {

	public abstract void setConnectionState(TncConnectionState imConnectionState);
	public abstract void requestMessages(TnccsSessionContext context);
	public abstract void forwardMessage(TnccsSessionContext context, TnccsMessageValue value);
}
