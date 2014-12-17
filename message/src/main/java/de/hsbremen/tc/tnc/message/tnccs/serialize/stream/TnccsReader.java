package de.hsbremen.tc.tnc.message.tnccs.serialize.stream;

import java.io.InputStream;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.TnccsData;

public interface TnccsReader<T extends TnccsData> {

	 public byte getMinDataLength(); 
	
	 public T read(final InputStream in, final long length) throws SerializationException, ValidationException;
}
