package de.hsbremen.tc.tnc.message.m.serialize.bytebuffer;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.ImData;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

public interface ImReader<T extends ImData> {

	 public byte getMinDataLength(); 
	
	 public T read(final ByteBuffer buffer, final long length) throws SerializationException, ValidationException;
}
