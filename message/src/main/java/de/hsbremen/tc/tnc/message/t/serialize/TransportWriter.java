package de.hsbremen.tc.tnc.message.t.serialize;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.t.TransportData;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

public interface TransportWriter<T extends TransportData> {

	 public void write(final T data, ByteBuffer buf) throws SerializationException;
	
}
