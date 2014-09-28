package de.hsbremen.tc.tnc.tnccs.serialize;

import java.io.InputStream;
import java.io.OutputStream;

import de.hsbremen.tc.tnc.tnccs.exception.SerializationException;
import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;

public interface TnccsSerializer<S> {
    
    public void encode(final S data, final OutputStream out) throws SerializationException;
    
    public S decode(final InputStream in, final long length) throws SerializationException, ValidationException;
}
