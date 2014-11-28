package de.hsbremen.tc.tnc.newp;

import de.hsbremen.tc.tnc.connection.TncConnectionState;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.im.session.enums.ImMessageTriggerEnum;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageValue;

public interface ImHandler {

	public abstract void setConnectionState(TncConnectionState imConnectionState) throws TncException;
	public abstract void triggerMessage(TnccsSessionContext context, ImMessageTriggerEnum trigger) throws TncException;
	public abstract void handleMessage(TnccsSessionContext context, TnccsMessageValue value) throws TncException;
}
