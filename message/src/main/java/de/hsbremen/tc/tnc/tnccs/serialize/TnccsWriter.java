package de.hsbremen.tc.tnc.tnccs.serialize;

import java.io.OutputStream;

import de.hsbremen.tc.tnc.tnccs.exception.SerializationException;

public interface TnccsWriter<T> {

	 public void write(final T data, final OutputStream out) throws SerializationException;
	
}
