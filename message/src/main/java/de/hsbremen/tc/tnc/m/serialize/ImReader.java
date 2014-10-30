package de.hsbremen.tc.tnc.m.serialize;

import java.io.InputStream;

import de.hsbremen.tc.tnc.exception.SerializationException;
import de.hsbremen.tc.tnc.exception.ValidationException;
import de.hsbremen.tc.tnc.m.ImData;

public interface ImReader<T extends ImData> {

	 public byte getMinDataLength(); 
	
	 public T read(final InputStream in, final long length) throws SerializationException, ValidationException;
}
