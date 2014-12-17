package de.hsbremen.tc.tnc.message.t.serialize.bytebuffer;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.t.TransportData;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

public interface TransportReader<T extends TransportData> {

	 public byte getMinDataLength(); 
	
	 public T read(ByteBuffer b, long length) throws SerializationException, ValidationException;
}
