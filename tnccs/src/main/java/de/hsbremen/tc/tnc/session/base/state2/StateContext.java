package de.hsbremen.tc.tnc.session.base.state2;

public interface StateContext {

	public void setState(SessionState state);
	public SessionState getState();
}
