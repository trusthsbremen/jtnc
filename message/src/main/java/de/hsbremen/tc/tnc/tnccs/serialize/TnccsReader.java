package de.hsbremen.tc.tnc.tnccs.serialize;

import java.io.InputStream;
import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;

import de.hsbremen.tc.tnc.tnccs.exception.SerializationException;

public interface TnccsReader<T> {

	 public byte getMinDataLength(); 
	
	 public T read(final InputStream in, final long length) throws SerializationException, ValidationException;
}
