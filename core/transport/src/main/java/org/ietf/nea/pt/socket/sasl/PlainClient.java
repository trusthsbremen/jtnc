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


public final class PlainClient implements SaslClient {

    private static final String MECH_NAME = "PLAIN";
    private static final byte SEP = '\000'; // NUL
    private boolean completed;
    private CallbackHandler callbackHandler;
    

    /**
     * Implements the PLAIN SASL client-side mechanism. (<A
     * HREF="http://www.ietf.org/rfc/rfc4616.txt">RFC 4616</A>).
     * 
     * @param callbackHandler the callback handler to use. 
     * @throws SaslException if callback handler is null.
     */
    PlainClient(CallbackHandler callbackHandler) throws SaslException {

        if (callbackHandler == null) {
            throw new SaslException(
                    "Callback handler to get username/password required.");
        }

        this.callbackHandler = callbackHandler;
        this.completed = false;
    }

    /**
     * Retrieves this mechanism's name for to initiate the PLAIN protocol
     * exchange.
     * 
     * @return The string "PLAIN".
     */
    public String getMechanismName() {
        return MECH_NAME;
    }

    public boolean hasInitialResponse() {
        return true;
    }

    public void dispose() throws SaslException {
        // Do nothing.
    }

    /**
     * Retrieves the initial response for the SASL command, which for PLAIN is
     * the concatenation of authorization ID, authentication ID and password,
     * with each component separated by the UTF-8 <NUL> byte.
     * 
     * @param challengeData Ignored
     * @return A non-null byte array containing the response to be sent to the
     * server.
     * @throws SaslException If cannot encode ids in UTF-8
     * @throw IllegalStateException if authentication already completed
     */
    public byte[] evaluateChallenge(byte[] challengeData) throws SaslException {
        if (this.completed) {
            throw new IllegalStateException(
                    "PLAIN authentication already completed");
        }
        
        this.completed = true;
        
        if(challengeData != null && challengeData .length > 0){
            throw new SaslException("PLAIN does not expect a non empty challenge.");
        }

        String authorizationId = null;
        String authenticationID = null;
        char[] password = null;
        
        try{
            NameCallback ncb1 = new NameCallback("PLAIN authorization id: ");
            
            this.callbackHandler.handle(new Callback[]{ncb1});
            authorizationId = ncb1.getName();
      
            NameCallback ncb2 = (authorizationId != null && !authorizationId
                    .isEmpty()) ? new NameCallback("PLAIN authentication id: ",
                    authorizationId)
                    : new NameCallback("PLAIN authentication id: ");
            
            
            PasswordCallback pcb = new PasswordCallback("PLAIN password: ", false);
            
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

        return this.createResponse(authorizationId, authenticationID, password);

    }

    private byte[] createResponse(String authorizationId,
            String authenticationID, char[] password) throws SaslException {
        
        byte[] authzid = null;
        byte[] authcid = new byte[0];
        
        try {
            authzid = (authorizationId != null) ? authorizationId
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

    /**
     * Determines whether this mechanism has completed. Plain completes after
     * returning one response.
     * 
     * @return true if has completed; false otherwise;
     */
    public boolean isComplete() {
        return this.completed;
    }

    /**
     * Unwraps the incoming buffer.
     * 
     * @throws SaslException Not applicable to this mechanism.
     */
    public byte[] unwrap(byte[] incoming, int offset, int len)
            throws SaslException {
        if (this.completed) {
            throw new SaslException(
                    "PLAIN supports neither integrity nor privacy.");
        } else {
            throw new IllegalStateException(
                    "PLAIN authentication not completed.");
        }
    }

    /**
     * Wraps the outgoing buffer.
     * 
     * @throws SaslException Not applicable to this mechanism.
     */
    public byte[] wrap(byte[] outgoing, int offset, int len)
            throws SaslException {
        if (this.completed) {
            throw new SaslException(
                    "PLAIN supports neither integrity nor privacy.");
        } else {
            throw new IllegalStateException(
                    "PLAIN authentication not completed.");
        }
    }

    /**
     * Retrieves the negotiated property. This method can be called only after
     * the authentication exchange has completed (i.e., when
     * <tt>isComplete()</tt> returns true); otherwise, a <tt>SaslException</tt>
     * is thrown.
     * 
     * @return value of property; only QOP is applicable to PLAIN.
     * @exception IllegalStateException if this authentication exchange has not
     * completed
     */
    public Object getNegotiatedProperty(String propName) {
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
