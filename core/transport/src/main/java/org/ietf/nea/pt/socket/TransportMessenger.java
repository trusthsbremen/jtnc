package org.ietf.nea.pt.socket;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.t.message.TransportMessage;
import de.hsbremen.tc.tnc.transport.exception.ConnectionException;

public interface TransportMessenger {

    /**
     * Returns a linear growing identifier for a transport message.
     * If the identifier reaches a value >= 4294967295 it restarts at zero.
     *
     * @return the growing identifier
     */
    long getIdentifier();

    /**
     * Write a transport message to an output stream
     * using a writer for serialization.
     *
     * @param message the transport message
     * @throws SerializationException if an error occurred at message
     * serialization
     * @throws ConnectionException if the connection is broken
     */
    void writeToStream(TransportMessage message)
            throws ConnectionException, SerializationException;

    /**
     * Reads a transport message from an input stream
     * using a reader for parsing.
     *
     * @return the container containing a transport message
     * and minor parsing exceptions if any
     * @throws SerializationException if an error occurred at message
     * serialization
     * @throws ValidationException if an error occurred at message parsing
     * @throws ConnectionException if the connection is broken
     */
    TransportMessage readFromStream()
            throws SerializationException, ValidationException,
            ConnectionException;

    /**
     * Creates a transport message containing a validation exception.
     *
     * @param exception the validation exception
     * @return the transport message
     * @throws ValidationException if message creation fails
     */
    TransportMessage createValidationErrorMessage(
            ValidationException exception) throws ValidationException;

}