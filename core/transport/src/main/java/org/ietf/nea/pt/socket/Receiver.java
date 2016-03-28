package org.ietf.nea.pt.socket;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.transport.exception.ConnectionException;

public interface Receiver {

    ByteBuffer getTnccsData();

    void receive(TransportMessenger wrappedSocket)
            throws SerializationException, ValidationException,
            ConnectionException;

}