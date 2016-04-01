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

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.sasl.Sasl;
import javax.security.sasl.SaslClient;
import javax.security.sasl.SaslException;

/**
 * Implements the PLAIN SASL client-side mechanism. (<A
 * HREF="http://www.ietf.org/rfc/rfc4616.txt">RFC 4616</A>)
 * It processes a name callback for the authentication ID
 * and a password callback for the associated password. 
 */
public final class PlainClient implements SaslClient {

    private static final String MECH_NAME = "PLAIN";
    private static final byte SEP = '\000'; // NUL
    private boolean completed;
    private CallbackHandler callbackHandler;
    private final String authorizationId;

    /**
     * Constructs a Plain mechanism without authorization ID.
     * 
     * @param callbackHandler the callback handler to use
     * @throws SaslException if callback handler is null
     */
    public PlainClient(final CallbackHandler callbackHandler)
            throws SaslException {
       this(null, callbackHandler);
    }
    
    /**
     * Constructs a Plain mechanism with optional authorization ID.
     * 
     * @param authorizationId If non-null, used to specify authorization ID
     * @param callbackHandler the callback handler to use
     * @throws SaslException if callback handler is null
     */
    public PlainClient(final String authorizationId,
            final CallbackHandler callbackHandler) throws SaslException {

        if (callbackHandler == null) {
            throw new SaslException(
                    "Callback handler to get username/password required.");
        }

        this.authorizationId = authorizationId;
        this.callbackHandler = callbackHandler;
       
        this.completed = false;
    }

    @Override
    public String getMechanismName() {
        return MECH_NAME;
    }

    @Override
    public boolean hasInitialResponse() {
        // mechanism has initial response
        return true;
    }

    @Override
    public void dispose() throws SaslException {
        // Do nothing.
    }

    @Override
    public byte[] evaluateChallenge(final byte[] challengeData)
            throws SaslException {
        if (this.completed) {
            throw new IllegalStateException(
                    "PLAIN authentication already completed");
        }
        
        this.completed = true;
        
        if (challengeData != null && challengeData .length > 0) {
            throw new SaslException("PLAIN does not expect "
                    + "a non empty challenge.",
                    new IllegalArgumentException(
                            "Challenge cannot be empty."));
        }

        String authenticationID = null;
        char[] password = null;
        
        try {

            NameCallback ncb2 = (this.authorizationId != null
                    && !this.authorizationId.isEmpty())
                    ? new NameCallback("PLAIN authentication id: ",
                    this.authorizationId)
                    : new NameCallback("PLAIN authentication id: ");
            
            
            PasswordCallback pcb = new PasswordCallback("PLAIN password: "
                    , false);
            
            this.callbackHandler.handle(new Callback[]{ncb2, pcb});
            authenticationID = ncb2.getName();
            password = pcb.getPassword();
            pcb.clearPassword();
            
        } catch (IOException e) {
            clearPassword(password);
            throw new SaslException("Cannot get credential information", e);
        } catch (UnsupportedCallbackException e) {
            clearPassword(password);
            throw new SaslException("Cannot get credential information", e);
        }

        if (authenticationID == null || password == null
                || authenticationID.isEmpty() || password.length <= 0) {
            clearPassword(password);
            throw new SaslException("PLAIN authentication failed either "
                    + "authentication ID or password is missing.");
        }

        return this.createResponse(authenticationID, password);

    }

    /**
     * Creates a response with the given authentication ID and password.
     * @param authenticationID the ID to use for authentication
     * @param password the password to authenticate the authentication ID
     * @return the response in raw bytes
     * @throws SaslException if byte encoding fails
     */
    private byte[] createResponse(final String authenticationID, 
            final char[] password) throws SaslException {
        
        byte[] authzid = null;
        byte[] authcid = new byte[0];
        
        try {
            authzid = (this.authorizationId != null) ? this.authorizationId
                    .getBytes("UTF8") : null;

            authcid = authenticationID.getBytes("UTF8");

        } catch (java.io.UnsupportedEncodingException e) {
            clearPassword(password);
            throw new SaslException("PLAIN no UTF-8 encoding", e);
        }

        byte[] response = new byte[password.length + authcid.length + 2
                + (authzid == null ? 0 : authzid.length)];

        int pos = 0;
        if (authzid != null) {
            System.arraycopy(authzid, 0, response, 0, authzid.length);
            pos = authzid.length;
        }
        response[pos++] = SEP;
        System.arraycopy(authcid, 0, response, pos, authcid.length);

        pos += authcid.length;
        response[pos++] = SEP;

        byte[] pwTemp = new byte[password.length];
        for (int i  = 0; i <  password.length; i++) {
            pwTemp[i] = (byte) (password[i] & 0x00FF);
        }
        clearPassword(password);
        
        System.arraycopy(pwTemp, 0, response, pos, pwTemp.length);

        for (int i = 0; i < pwTemp.length; i++) {
            pwTemp[i] = (byte) 0;
        }
        pwTemp = null;
        
        return response;
    }

    @Override
    public boolean isComplete() {
        return this.completed;
    }

    @Override
    public byte[] unwrap(final byte[] incoming, final int offset,
            final int len) throws SaslException {
        if (this.completed) {
            throw new SaslException(
                    "PLAIN supports neither integrity nor privacy.");
        } else {
            throw new IllegalStateException(
                    "PLAIN authentication not completed.");
        }
    }

    @Override
    public byte[] wrap(final byte[] outgoing, final int offset, final int len)
            throws SaslException {
        if (this.completed) {
            throw new SaslException(
                    "PLAIN supports neither integrity nor privacy.");
        } else {
            throw new IllegalStateException(
                    "PLAIN authentication not completed.");
        }
    }

    @Override
    public Object getNegotiatedProperty(final String propName) {
        if (this.completed) {
            if (propName.equals(Sasl.QOP)) {
                return "auth";
            } else {
                return null;
            }
        } else {
            throw new IllegalStateException(
                    "PLAIN authentication not completed");
        }
    }

    /**
     * Clears the password in memory using zeros.
     * @param password the reference to the password to clear 
     */
    private void clearPassword(char[] password) {
        if (password != null) {
            // zero out password
            for (int i = 0; i < password.length; i++) {
                password[i] = (byte) 0;
            }
            password = null;
        }
    }
}
