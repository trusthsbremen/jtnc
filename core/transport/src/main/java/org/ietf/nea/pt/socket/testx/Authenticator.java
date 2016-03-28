package org.ietf.nea.pt.socket.testx;

import org.ietf.nea.pt.value.enums.PtTlsSaslResultEnum;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.transport.exception.ConnectionException;

public interface Authenticator {

    /**
     * Handles the authentication phase with a remote peer.
     * This implementation does not support additional authentication.
     * If the remote peer requests additional authentication,
     * a transport message containing an error is send.
     * @return 
     *
     * @throws SerializationException if an error occurred at message
     * serialization
     * @throws ValidationException if an error occurred at message parsing
     * @throws ConnectionException if the connection is broken
     */
    void authenticate(TransportMessenger connection)
            throws ValidationException, ConnectionException,
            SerializationException;

    /**
     * Returns the result of the authentication.
     * @return the result or null if unknown because authentication is pending
     */
    PtTlsSaslResultEnum getAuthenticationResult();

}