package de.hsbremen.tc.tnc.tnccs.serialize;

import java.io.InputStream;

import de.hsbremen.tc.tnc.exception.SerializationException;
import de.hsbremen.tc.tnc.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.TnccsData;

public interface TnccsReader<T extends TnccsData> {

	 public byte getMinDataLength(); 
	
	 public T read(final InputStream in, final long length) throws SerializationException, ValidationException;
}
