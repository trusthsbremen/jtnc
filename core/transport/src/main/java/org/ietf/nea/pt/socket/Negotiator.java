package org.ietf.nea.pt.socket;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.transport.exception.ConnectionException;

public interface Negotiator {

    /**
     * Handles the transport protocol version negotiation phase
     * with a remote peer.
     *
     * @throws SerializationException if an error occurred at message
     * serialization
     * @throws ValidationException if an error occurred at message parsing
     * @throws ConnectionException if the connection is broken
     */
    void negotiate(TransportMessenger connection) throws SerializationException,
            ValidationException, ConnectionException;
    
    /**
     * Returns the negotiated version number.
     * @return the negotiated version or 0 if version negotiation is pending or has failed
     */
    short getNegotiatedVersion();

}