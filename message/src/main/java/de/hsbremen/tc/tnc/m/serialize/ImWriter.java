package de.hsbremen.tc.tnc.m.serialize;

import java.io.OutputStream;

import de.hsbremen.tc.tnc.exception.SerializationException;

public interface ImWriter<T> {

	 public void write(final T data, final OutputStream out) throws SerializationException;
	
}
