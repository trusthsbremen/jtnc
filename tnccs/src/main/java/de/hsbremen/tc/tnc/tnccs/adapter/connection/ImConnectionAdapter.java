package de.hsbremen.tc.tnc.tnccs.adapter.connection;

public interface ImConnectionAdapter {

	public abstract void allowMessageReceipt();

	public abstract void denyMessageReceipt();
	
	public abstract boolean isReceiving();

	public abstract long getImId();
}