package de.hsbremen.tc.tnc.adapter.im;

import de.hsbremen.tc.tnc.connection.TncConnectionState;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageValue;

public interface ImAdapter<T> {
	
	public abstract long getPrimaryId();
	
	public abstract void notifyConnectionChange(T connection,
			TncConnectionState state) throws TncException, TerminatedException;

	public abstract void beginHandshake(T connection)
			throws TncException, TerminatedException;
	
	public abstract void handleMessage(T connection,
			TnccsMessageValue message) throws TncException, TerminatedException;

	public abstract void batchEnding(T connection)
			throws TncException, TerminatedException;

	public abstract void terminate() throws TerminatedException;
}