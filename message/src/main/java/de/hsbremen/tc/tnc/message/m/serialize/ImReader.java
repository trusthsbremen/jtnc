package de.hsbremen.tc.tnc.message.m.serialize;

import java.io.InputStream;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.ImData;

public interface ImReader<T extends ImData> {

	 public byte getMinDataLength(); 
	
	 public T read(final InputStream in, final long length) throws SerializationException, ValidationException;
}
