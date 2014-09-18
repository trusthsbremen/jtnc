package de.hsbremen.tc.tnc.session.context;

public interface TncSession extends Runnable{

	// called by tncc
	public abstract void requestHandshake();

	public abstract void cancel();

}