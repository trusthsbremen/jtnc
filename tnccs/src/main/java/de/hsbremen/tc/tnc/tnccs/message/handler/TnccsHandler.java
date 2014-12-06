package de.hsbremen.tc.tnc.tnccs.message.handler;

import de.hsbremen.tc.tnc.connection.TncConnectionState;

public interface TnccsHandler extends TnccsMessageHandler {

	public abstract TncConnectionState getAccessDecision();
	
}
