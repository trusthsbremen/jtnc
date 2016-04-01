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
package org.ietf.nea.pt.socket.sasl;

import java.io.IOException;
import java.nio.charset.Charset;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.sasl.AuthorizeCallback;
import javax.security.sasl.SaslException;
import javax.security.sasl.SaslServer;

/**
 * Implements the EXTERNAL SASL server-side mechanism. (<A
 * HREF="http://www.ietf.org/rfc/rfc2222.txt">RFC 2222</A>).
 * It processes an authorization callback for the requested
 * authorization ID. 
 */
public class ExternalServer implements SaslServer {

    private static final String MECH_NAME = "EXTERNAL";
    private static final int MAX_MESSAGE_LENGTH = 65536;
    private boolean completed;
    private final CallbackHandler callbackHandler;
    private String authorizationId;
    
    /**
     * Constructs an External mechanism.
     * 
     * @param callbackHandler the callback handler to use
     * @throws SaslException if callback handler is null
     */
    public ExternalServer(final CallbackHandler callbackHandler)
            throws SaslException {
        if (callbackHandler == null) {
            throw new SaslException(
                    "Callback for authorization required.");
        }
        
        this.callbackHandler = callbackHandler;
        this.completed = false;
    }

    @Override
    public String getMechanismName() {
        return MECH_NAME;
    }

    @Override
    public byte[] evaluateResponse(final byte[] response)
            throws SaslException {
        if (this.completed) {
            throw new IllegalStateException(
                    "EXTERNAL authentication already completed");
        }

        this.completed = true;

        this.checkResponse(response);
        
        String authzid = new String(response, Charset.forName("UTF8"));

        try {
            NameCallback ncb = new NameCallback(
                    "EXTERNAL authentication id: ");
            this.callbackHandler.handle(new Callback[] { ncb});
            String authcid = ncb.getName();
            
            if (authcid == null || authcid.isEmpty()) {
                throw new SaslException("PLAIN authentication failed.");
            }
            
            AuthorizeCallback acb = new AuthorizeCallback(authcid,
                    (authzid == null || authzid.isEmpty()) ? authcid
                            : authzid);

            this.callbackHandler.handle(new Callback[] { acb });

            if (!acb.isAuthorized()) {
                throw new SaslException("EXTERNAL authentication failed.");
            } else {
                this.authorizationId = acb.getAuthorizedID();
            }

        } catch (IOException e) {
            // SaslException is of type IOException!
            if (e instanceof SaslException) {
                throw (SaslException) e;
            }
            throw new SaslException("Cannot get credential information", e);
        } catch (UnsupportedCallbackException e) {
            throw new SaslException("Cannot get credential information", e);
        }

        return null;
    }

    @Override
    public boolean isComplete() {
        return this.completed;
    }

    @Override
    public String getAuthorizationID() {
        if (!this.completed) {
            throw new IllegalStateException(
                    "PLAIN authentication not completed.");
        }
        
        return this.authorizationId;
    }

    @Override
    public byte[] unwrap(final byte[] incoming, final int offset,
            final int len) throws SaslException {
        if (completed) {
            throw new SaslException(
                    "EXTERNAL supports neither integrity nor privacy.");
        } else {
            throw new IllegalStateException(
                    "EXTERNAL authentication Not completed.");
        }
    }

    @Override
    public byte[] wrap(final byte[] outgoing, final int offset, final int len)
            throws SaslException {
        if (completed) {
            throw new SaslException(
                    "EXTERNAL supports neither integrity nor privacy.");
        } else {
            throw new IllegalStateException(
                    "EXTERNAL authentication Not completed.");
        }
    }

    @Override
    public Object getNegotiatedProperty(final String propName) {
        if (completed) {
            return null;
        } else {
            throw new IllegalStateException(
                    "EXTERNAL authentication not completed");
        }
    }

    @Override
    public void dispose() throws SaslException {
        // Do nothing.

    }
    
    /**
     * Checks if the format of the received response is valid.
     * @param response the response in raw bytes to check
     * @throws SaslException if message format is not valid
     */
    private void checkResponse(final byte[] response) throws SaslException {
        if (response == null || response.length <= 0) {
            throw new SaslException("EXTERNAL response cannot be empty.");
        } else {
            if (response.length >= MAX_MESSAGE_LENGTH) {
                throw new SaslException(
                        "PLAIN Message to long. This implementation"
                                + " does support a maximum length of "
                                + MAX_MESSAGE_LENGTH + " byte.");
            }
        }
    }

}
