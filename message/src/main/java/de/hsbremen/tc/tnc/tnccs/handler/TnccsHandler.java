package de.hsbremen.tc.tnc.tnccs.handler;

import de.hsbremen.tc.tnc.tnccs.exception.HandlingException;

public interface TnccsHandler<T> {

	 public abstract void handle(final T structure) throws HandlingException;
	 
}
