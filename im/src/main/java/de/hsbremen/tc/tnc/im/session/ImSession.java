package de.hsbremen.tc.tnc.im.session;

import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.im.adapter.ImConnectionStateEnum;
import de.hsbremen.tc.tnc.im.adapter.data.ImObjectComponent;
import de.hsbremen.tc.tnc.im.session.enums.ImMessageTriggerEnum;

interface ImSession {
	
	// TODO use costume exceptions where possible.

//	public abstract ImConnectionStateEnum getConnectionState();
	
	public abstract void setConnectionState(ImConnectionStateEnum connectionState) throws TncException;

	public abstract void triggerMessage(ImMessageTriggerEnum reason) throws TncException;

	public abstract void handleMessage(ImObjectComponent component) throws TncException;

	public abstract void terminate();

}