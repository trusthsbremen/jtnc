package de.hsbremen.tc.tnc.message.tnccs.serialize.bytebuffer;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.tnccs.TnccsData;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

public interface TnccsWriter<T extends TnccsData> {

	 public void write(final T data, final ByteBuffer buffer) throws SerializationException;
	
}
