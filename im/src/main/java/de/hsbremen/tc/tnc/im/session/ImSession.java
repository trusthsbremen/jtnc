package de.hsbremen.tc.tnc.im.session;

import de.hsbremen.tc.tnc.connection.TncConnectionState;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.im.adapter.data.ImObjectComponent;
import de.hsbremen.tc.tnc.im.session.enums.ImMessageTriggerEnum;

interface ImSession {
	
	// TODO use costume exceptions where possible.

//	public abstract ImConnectionStateEnum getConnectionState();
	
	public abstract void setConnectionState(TncConnectionState imConnectionState) throws TncException;

	public abstract void triggerMessage(ImMessageTriggerEnum trigger) throws TncException;

	public abstract <T extends ImObjectComponent> void handleMessage(T component) throws TncException;

	public abstract void terminate();

}