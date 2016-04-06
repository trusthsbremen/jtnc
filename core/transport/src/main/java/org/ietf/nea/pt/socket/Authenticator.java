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

import org.ietf.nea.pt.socket.enums.AuthenticatorTypeEnum;
import org.ietf.nea.pt.value.enums.PtTlsSaslResultEnum;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.transport.exception.ConnectionException;

/**
 * Generic authentication handler for an IETF socket transport connection.
 * It uses the SASL framework to authenticate the connection. 
 */
public interface Authenticator {

    /**
     * Returns the type of the implemented authenticator.
     * @return the type as enum constant (Server or Client)
     */
    AuthenticatorTypeEnum getType();
    
    /**
     * Executes the authentication with a remote peer.
     *
     * @throws SerializationException if an error occurs at message
     * serialization
     * @throws ValidationException if an error occurs at message parsing
     * @throws ConnectionException if the connection is broken
     */
    void authenticate(SocketMessenger connection)
            throws ValidationException, ConnectionException,
            SerializationException;

    /**
     * Returns the result of the authentication after a call to
     * {@link #authenticate(SocketMessenger)}.
     * 
     * @return the result or null if unknown because authentication is pending
     */
    PtTlsSaslResultEnum getAuthenticationResult();

}
