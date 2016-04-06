/**
 * The BSD 3-Clause License ("BSD New" or "BSD Simplified")
 *
 * Copyright © 2015 Trust HS Bremen and its Contributors. All rights   
 * reserved.
 *
 * See the CONTRIBUTORS file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER
 * OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.ietf.nea.pt.socket;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.t.message.TransportMessage;
import de.hsbremen.tc.tnc.transport.exception.ConnectionException;

/**
 * Generic message handler to hold a socket and mask its functions
 * for the transmission and receipt of TNC messages thru the
 * contained socket.
 */
public interface SocketMessenger {

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
     * @throws SerializationException if an error occurs at message
     * serialization
     * @throws ConnectionException if the connection is broken
     */
    void writeToStream(TransportMessage message)
            throws ConnectionException, SerializationException;

    /**
     * Reads a transport message from an input stream
     * using a reader for parsing.
     *
     * @return the transport message
     * @throws SerializationException if an error occurs at message
     * serialization
     * @throws ValidationException if an error occurs at message parsing
     * @throws ConnectionException if the connection is broken
     */
    TransportMessage readFromStream()
            throws SerializationException, ValidationException,
            ConnectionException;

    /**
     * Creates an transport message of type error containing
     * a validation exception.
     *
     * @param exception the validation exception
     * @return the transport message
     * @throws ValidationException if message creation fails
     */
    TransportMessage createValidationErrorMessage(
            ValidationException exception) throws ValidationException;

}
