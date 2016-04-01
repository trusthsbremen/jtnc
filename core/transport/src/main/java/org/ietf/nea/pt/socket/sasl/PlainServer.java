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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.sasl.AuthorizeCallback;
import javax.security.sasl.Sasl;
import javax.security.sasl.SaslException;
import javax.security.sasl.SaslServer;

/**
 * Implements the PLAIN SASL server-side mechanism. (<A
 * HREF="http://www.ietf.org/rfc/rfc4616.txt">RFC 4616</A>)
 * It processes a name callback for the authentication ID
 * and a password callback for the associated password as well
 * as an authorization callback for the given credentials. 
 */
public class PlainServer implements SaslServer {

    private static final String MECH_NAME = "PLAIN";
    private static final byte SEP = '\000'; // NUL
    private static final int MAX_MESSAGE_LENGTH = 65536;
    private boolean completed;
    private CallbackHandler callbackHandler;
    private String authorizationId;

    /**
     * Constructs a Plain mechanism.
     * 
     * @param callbackHandler the callback handler to use
     * @throws SaslException if callback handler is null
     */
    public PlainServer(final CallbackHandler callbackHandler)
            throws SaslException {
        if (callbackHandler == null) {
            throw new SaslException(
                    "Callback handler to get username/password required.");
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
                    "PLAIN authentication already completed");
        }

        this.completed = true;

        this.checkResponse(response);

        List<byte[]> tokens = this.tokenize(response);

        String authzid;
        String authcid;
        char[] password;
        
        if (tokens.size() > 2) {
            authzid = new String(tokens.get(0), Charset.forName("UTF8"));
            authcid = new String(tokens.get(1), Charset.forName("UTF8"));
            password = new char[tokens.get(2).length];

            for (int i = 0; i < tokens.get(2).length; i++) {
                password[i] = (char) tokens.get(2)[i];
            }

            // remove password temp storage
            for (int i = 0; i < tokens.get(2).length; i++) {
                tokens.get(2)[i] = (byte) 0;
            }
            tokens.remove(2);

        } else {
            authzid = null;
            authcid = new String(tokens.get(0), Charset.forName("UTF8"));
            password = new char[tokens.get(1).length];

            for (int i = 0; i < tokens.get(1).length; i++) {
                password[i] = (char) tokens.get(1)[i];
            }

            // remove password temp storage
            for (int i = 0; i < tokens.get(1).length; i++) {
                tokens.get(1)[i] = (byte) 0;
            }
            tokens.remove(1);
        }

        this.authenticate(authzid, authcid, password);

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
            final int len)
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

    @Override
    public void dispose() throws SaslException {
        // Do nothing. 
        
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

    /**
     * Checks if the format of the received response is valid.
     * @param response the response in raw bytes to check
     * @throws SaslException if message format is not valid
     */
    private void checkResponse(final byte[] response) throws SaslException {
        if (response == null || response.length <= 0) {
            throw new SaslException("PLAIN response cannot be empty.",
                    new IllegalArgumentException("Response cannot be empty."));
        } else {
        
            if (response.length >= MAX_MESSAGE_LENGTH) {
                throw new SaslException(
                        "PLAIN Message to long.",
                        new IllegalArgumentException("Message to long."
                                + " This implementation"
                                + " does support a maximum length of "
                                + MAX_MESSAGE_LENGTH + " byte."));
            }
            
            {
                // check if all separators are in place an  message has
                // the RFC format
                int i = 0;
                int separatorCount = 0;
                while (i < response.length && separatorCount < 2) {
                    if (response[i] == 0) {
                        separatorCount++;
                    }
                    i++;
                }
                if (separatorCount < 2 || separatorCount > 2) {
                    throw new SaslException(
                            "PLAIN Message format is invalid.",
                            new IllegalArgumentException(
                                    "Message separator count does not fit."));
                }
            }
        }
    }
    
    /**
     * Splits the fields of the given response into a List of field tokens.
     * @param response the raw response containing the fields
     * @return a list of field tokens as raw byte arrays
     */
    private List<byte[]> tokenize(final byte[] response) {
        // tokenize
        int pos = -1;
        List<byte[]> token = new ArrayList<byte[]>(3);
        for (int i = 0; i < response.length; i++) {
            if (response[i] == SEP) {
                byte[] temp = new byte[i - (pos + 1)];
                System.arraycopy(response, pos + 1, temp, 0, i - (pos + 1));
                token.add(temp);
                pos = i;
            }
            if (i == response.length - 1) {
                byte[] temp = new byte[response.length - (pos + 1)];
                System.arraycopy(response, pos + 1, temp, 0, response.length
                        - (pos + 1));
                token.add(temp);
                pos = response.length;
            }
        }
        
        return token;
    }
    
    /**
     * Authenticates the given credentials using callbacks.
     *
     * @param authzid the given authorization ID
     * @param authcid the given authentication ID
     * @param password the given password
     * @throws SaslException if authentication fails due to credential mismatch
     * or internal error.
     */
    private void authenticate(final String authzid, final String authcid,
            final char[] password) throws SaslException {
        
        try {
            NameCallback ncb = new NameCallback("PLAIN authentication id: ",
                    authcid);

            PasswordCallback pcb = new PasswordCallback("PLAIN password: ",
                    false);

            this.callbackHandler.handle(new Callback[] { ncb, pcb });
            String authenticationID = ncb.getName();
            char[] expectedPassword = pcb.getPassword();
            pcb.clearPassword();

            if (authenticationID == null
                    || authenticationID.isEmpty()
                    || expectedPassword == null
                    || expectedPassword.length == 0) {

                clearPassword(password);
                clearPassword(expectedPassword);

                throw new SaslException("PLAIN authentication failed.");
            }

            if (authenticationID.equals(authcid)
                    && Arrays.equals(expectedPassword, password)) {

                clearPassword(password);
                clearPassword(expectedPassword);

                AuthorizeCallback acb = new AuthorizeCallback(authcid,
                        (authzid == null || authzid.isEmpty()) ? authcid
                                : authzid);

                callbackHandler.handle(new Callback[] { acb });

                if (!acb.isAuthorized()) {
                    throw new SaslException("PLAIN authentication failed.");
                }
                
                this.authorizationId = acb.getAuthorizedID();

            } else {
                clearPassword(password);
                clearPassword(expectedPassword);
                throw new SaslException("PLAIN authentication failed.");
            }

        } catch (IOException e) {
            // SaslException is of type IOException!
            clearPassword(password);
            if (e instanceof SaslException) {
                throw (SaslException) e;
            }
            throw new SaslException("Cannot get credential information", e);
        } catch (UnsupportedCallbackException e) {
            clearPassword(password);
            throw new SaslException("Cannot get credential information", e);
        } catch (IllegalArgumentException e) {
            clearPassword(password);
            throw new SaslException("Cannot get credential information", e);
        }
        
    }
}
