package de.hsbremen.tc.tnc.tnccs.im.enums;

import java.util.List;

import de.hsbremen.tc.tnc.connection.TncConnectionState;

public interface ImHandlerStateFactory {

	public ImHandlerState fromState(long state);
	public ImHandlerState fromConnectionState(TncConnectionState state);
	public List<ImHandlerState> getAllStates();
}
