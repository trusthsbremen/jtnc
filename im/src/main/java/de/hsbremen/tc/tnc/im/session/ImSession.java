package de.hsbremen.tc.tnc.im.session;

import org.trustedcomputinggroup.tnc.ifimc.TNCException;

import de.hsbremen.tc.tnc.im.adapter.ImConnectionStateEnum;
import de.hsbremen.tc.tnc.im.adapter.data.ImObjectComponent;
import de.hsbremen.tc.tnc.im.session.enums.ImMessageTriggerEnum;

public interface ImSession {
	
	// TODO use costume exceptions where possible.

//	public abstract ImConnectionStateEnum getConnectionState();
	
	public abstract void setConnectionState(ImConnectionStateEnum connectionState) throws TNCException;

	public abstract void triggerMessage(ImMessageTriggerEnum reason) throws TNCException;

	public abstract void handleMessage(ImObjectComponent component) throws TNCException;

	public abstract void terminate();

}