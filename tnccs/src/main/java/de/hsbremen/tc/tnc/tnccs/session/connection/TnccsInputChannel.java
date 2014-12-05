package de.hsbremen.tc.tnc.tnccs.session.connection;

public interface TnccsInputChannel extends Runnable{
	
	public abstract void register(TnccsInputChannelListener listener);

}