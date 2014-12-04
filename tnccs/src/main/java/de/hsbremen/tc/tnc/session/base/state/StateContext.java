package de.hsbremen.tc.tnc.session.base.state;

public interface StateContext {

	public void setState(SessionState state);
	public SessionState getState();
}
