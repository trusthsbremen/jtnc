package de.hsbremen.tc.tnc.message.m.serialize;

import java.io.OutputStream;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.m.ImData;

public interface ImWriter<T extends ImData> {
	
	 public void write(final T data, final OutputStream out) throws SerializationException;
	
}
