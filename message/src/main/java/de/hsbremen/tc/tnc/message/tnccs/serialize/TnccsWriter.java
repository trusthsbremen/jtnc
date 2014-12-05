package de.hsbremen.tc.tnc.message.tnccs.serialize;

import java.io.OutputStream;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.tnccs.TnccsData;

public interface TnccsWriter<T extends TnccsData> {

	 public void write(final T data, final OutputStream out) throws SerializationException;
	
}
