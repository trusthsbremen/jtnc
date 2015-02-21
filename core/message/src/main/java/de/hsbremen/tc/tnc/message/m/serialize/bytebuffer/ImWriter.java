package de.hsbremen.tc.tnc.message.m.serialize.bytebuffer;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.m.ImData;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

public interface ImWriter<T extends ImData> {
	
	 public void write(final T data, final ByteBuffer buffer) throws SerializationException;
	
}
