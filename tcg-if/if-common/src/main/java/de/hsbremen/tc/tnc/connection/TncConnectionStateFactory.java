package de.hsbremen.tc.tnc.connection;

import java.util.List;

public interface TncConnectionStateFactory {

	public abstract TncConnectionState fromState(long status);

	public abstract List<TncConnectionState> getAllStates();

}
