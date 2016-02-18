package org.ietf.nea.pt.socket;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.transport.exception.ConnectionException;

public interface VersionNegotiator {

    /**
     * Handles the transport protocol version negotiation phase
     * with a remote peer.
     *
     * @throws SerializationException if an error occurred at message
     * serialization
     * @throws ValidationException if an error occurred at message parsing
     * @throws ConnectionException if the connection is broken
     */
    public abstract void negotiate(AbstractSocketTransportConnection connection) throws SerializationException,
            ValidationException, ConnectionException;

}