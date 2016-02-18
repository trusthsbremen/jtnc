package org.ietf.nea.pt.socket.sasl;

import java.io.IOException;
import java.nio.charset.Charset;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.sasl.AuthorizeCallback;
import javax.security.sasl.SaslException;
import javax.security.sasl.SaslServer;

public class ExternalServer implements SaslServer {

    private static final String MECH_NAME = "EXTERNAL";
    private final static int MAX_MESSAGE_LENGTH = 65536;
    private boolean completed;
    private CallbackHandler callbackHandler;
    private String authorizationId;

    @Override
    public String getMechanismName() {
        return MECH_NAME;
    }

    @Override
    public byte[] evaluateResponse(byte[] response) throws SaslException {
        if (this.completed) {
            throw new IllegalStateException(
                    "EXTERNAL authentication already completed");
        }

        this.completed = true;

        this.checkResponse(response);
        
        String authzid = new String(response, Charset.forName("UTF8"));

        try {

            AuthorizeCallback acb = new AuthorizeCallback(authzid, authzid);

            callbackHandler.handle(new Callback[] { acb });

            if (!acb.isAuthorized()) {
                throw new SaslException("PLAIN authentication failed.");
            } else {
                this.authorizationId = acb.getAuthorizedID();
            }

        } catch (IOException e) {
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
        return this.authorizationId;
    }

    @Override
    public byte[] unwrap(byte[] incoming, int offset, int len)
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
    public byte[] wrap(byte[] outgoing, int offset, int len)
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
    public Object getNegotiatedProperty(String propName) {
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
    
    private void checkResponse(byte[] response) throws SaslException {
        if (response == null || response.length <= 0) {
            throw new SaslException("EXTERNAL response cannot be empty.");
        }else{
            if (response.length >= MAX_MESSAGE_LENGTH) {
                throw new SaslException(
                        "PLAIN Message to long. This implementation"
                                + " does support a maximum length of "
                                + MAX_MESSAGE_LENGTH + " byte.");
            }
        }
    }

}
