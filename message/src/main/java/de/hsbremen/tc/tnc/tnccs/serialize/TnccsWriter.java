package de.hsbremen.tc.tnc.tnccs.serialize;

import java.io.OutputStream;

import de.hsbremen.tc.tnc.exception.SerializationException;
import de.hsbremen.tc.tnc.tnccs.TnccsData;

public interface TnccsWriter<T extends TnccsData> {

	 public void write(final T data, final OutputStream out) throws SerializationException;
	
}
