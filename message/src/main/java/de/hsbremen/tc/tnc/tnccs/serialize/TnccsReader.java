package de.hsbremen.tc.tnc.tnccs.serialize;

import java.io.InputStream;

import de.hsbremen.tc.tnc.tnccs.exception.SerializationException;
import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;
@Deprecated
public interface TnccsReader<T> {

	 public T read(final InputStream in, final long length) throws SerializationException, ValidationException;
}
