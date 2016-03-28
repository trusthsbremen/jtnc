package org.ietf.nea.pt.socket.testx;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.transport.exception.ConnectionException;

public interface Receiver {

    public abstract ByteBuffer getTnccsData();

    public abstract void receive(TransportMessenger wrappedSocket)
            throws SerializationException, ValidationException,
            ConnectionException;

}