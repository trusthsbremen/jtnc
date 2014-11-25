package de.hsbremen.tc.tnc.connection;

import java.util.List;

public interface ImConnectionStateFactory {

	public abstract ImConnectionState fromState(long status);

	public abstract List<ImConnectionState> getAllStates();

}
