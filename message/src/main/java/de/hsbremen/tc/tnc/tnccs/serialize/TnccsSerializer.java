package de.hsbremen.tc.tnc.tnccs.serialize;

import java.io.InputStream;
import java.io.OutputStream;

import org.ietf.nea.pb.exception.RuleException;

import de.hsbremen.tc.tnc.tnccs.exception.SerializationException;

public interface TnccsSerializer<S> {
    
    public void encode(final S data, final OutputStream out) throws SerializationException;
    
    public S decode(final InputStream in, final long length) throws SerializationException, RuleException;
}
