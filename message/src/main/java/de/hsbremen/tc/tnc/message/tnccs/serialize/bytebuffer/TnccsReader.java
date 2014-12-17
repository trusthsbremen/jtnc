package de.hsbremen.tc.tnc.message.tnccs.serialize.bytebuffer;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.TnccsData;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

public interface TnccsReader<T extends TnccsData> {

	 public byte getMinDataLength(); 
	
	 public T read(final ByteBuffer buffer, final long length) throws SerializationException, ValidationException;
}
