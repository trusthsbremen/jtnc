package org.ietf.nea.pt.socket;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.transport.exception.ConnectionException;

public interface Authenticator {

    /**
     * Handles the authentication phase with a remote peer.
     * This implementation does not support additional authentication.
     * If the remote peer requests additional authentication,
     * a transport message containing an error is send.
     *
     * @throws SerializationException if an error occurred at message
     * serialization
     * @throws ValidationException if an error occurred at message parsing
     * @throws ConnectionException if the connection is broken
     */
    void authenticate(AbstractSocketTransportConnection connection)
            throws ValidationException, ConnectionException,
            SerializationException;

}