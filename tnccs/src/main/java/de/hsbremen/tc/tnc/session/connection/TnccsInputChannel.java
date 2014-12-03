package de.hsbremen.tc.tnc.session.connection;

public interface TnccsInputChannel extends Runnable{
	
	public abstract void register(BatchReceiver listener);

}