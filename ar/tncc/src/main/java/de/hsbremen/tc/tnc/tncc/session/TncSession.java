package de.hsbremen.tc.tnc.tncc.session;

public interface TncSession extends Runnable{

	// called by tncc and session
	public abstract void requestHandshake();

	public abstract void close();

}