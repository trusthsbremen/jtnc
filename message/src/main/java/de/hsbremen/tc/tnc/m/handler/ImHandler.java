package de.hsbremen.tc.tnc.m.handler;

import de.hsbremen.tc.tnc.exception.HandlingException;

public interface ImHandler<T> {

	 public abstract void handle(final T structure) throws HandlingException;
	 
}
