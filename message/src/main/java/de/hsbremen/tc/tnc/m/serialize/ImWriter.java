package de.hsbremen.tc.tnc.m.serialize;

import java.io.OutputStream;

import de.hsbremen.tc.tnc.exception.SerializationException;
import de.hsbremen.tc.tnc.m.ImData;

public interface ImWriter<T extends ImData> {
	
	 public void write(final T data, final OutputStream out) throws SerializationException;
	
}
