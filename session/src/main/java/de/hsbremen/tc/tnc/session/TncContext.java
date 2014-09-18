package de.hsbremen.tc.tnc.session;

import de.hsbremen.tc.tnc.session.context.enums.SessionEventEnum;

public interface TncContext {
	
	public abstract boolean isServer();
	
	public abstract void notifyClient(String sessionId, SessionEventEnum event, Object updateData);
	
	// returns -1 one if no imcIds are left
	public abstract long reserveImId();

}
