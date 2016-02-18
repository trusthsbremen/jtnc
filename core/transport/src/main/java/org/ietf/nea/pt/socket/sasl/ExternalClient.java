package org.ietf.nea.pt.socket.sasl;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.sasl.SaslClient;
import javax.security.sasl.SaslException;

/**
 * Implements the EXTERNAL SASL client mechanism. (<A
 * HREF="http://www.ietf.org/rfc/rfc2222.txt">RFC 2222</A>). The EXTERNAL
 * mechanism returns the optional authorization ID as the initial response. It
 * processes no challenges.
 * 
 */
public class ExternalClient implements SaslClient {

    private static final String MECH_NAME = "EXTERNAL";
    private boolean completed;
    private CallbackHandler callbackHandler;

    /**
     * Constructs an External mechanism with optional authorization ID.
     * 
     * @param authorizationID If non-null, used to specify authorization ID.
     * @throws SaslException if cannot convert authorizationID into UTF-8
     * representation.
     */
    ExternalClient(CallbackHandler callbackHandler) throws SaslException {
        if (callbackHandler == null) {
            throw new SaslException("Callback handler to get authorization id.");
        }
        this.callbackHandler = callbackHandler;
        this.completed = false;
    }

    /**
     * Retrieves this mechanism's name for initiating the "EXTERNAL" protocol
     * exchange.
     * 
     * @return The string "EXTERNAL".
     */
    public String getMechanismName() {
        return MECH_NAME;
    }

    /**
     * This mechanism has an initial response.
     */
    public boolean hasInitialResponse() {
        return true;
    }

    public void dispose() throws SaslException {
        // Do nothing.
    }

    /**
     * Processes the challenge data. It returns the EXTERNAL mechanism's initial
     * response, which is the authorization id encoded in UTF-8. This is the
     * optional information that is sent along with the SASL command. After this
     * method is called, isComplete() returns true.
     * 
     * @param challengeData Ignored.
     * @return The possible empty initial response.
     * @throws SaslException If authentication has already been called.
     */
    public byte[] evaluateChallenge(byte[] challengeData) throws SaslException {
        if (completed) {
            throw new IllegalStateException(
                    "EXTERNAL authentication already completed");
        }

        completed = true;

        String authorizationId = null;

        try {
            NameCallback ncb1 = new NameCallback("PLAIN authorization id: ");

            this.callbackHandler.handle(new Callback[] { ncb1 });
            authorizationId = ncb1.getName();

        } catch (IOException e) {
            throw new SaslException("Cannot get credential information", e);
        } catch (UnsupportedCallbackException e) {
            throw new SaslException("Cannot get credential information", e);
        }

        byte[] authzid = null;

        try {
            authzid = (authorizationId != null) ? authorizationId
                    .getBytes("UTF8") : null;
        } catch (java.io.UnsupportedEncodingException e) {
            throw new SaslException("PLAIN no UTF-8 encoding", e);
        }

        return (authzid != null) ? authzid : new byte[0];
    }

    /**
     * Returns whether this mechanism is complete.
     * 
     * @return true if initial response has been sent; false otherwise.
     */
    public boolean isComplete() {
        return completed;
    }

    /**
     * Unwraps the incoming buffer.
     * 
     * @throws SaslException Not applicable to this mechanism.
     */
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

    /**
     * Wraps the outgoing buffer.
     * 
     * @throws SaslException Not applicable to this mechanism.
     */
    public byte[] wrap(byte[] outgoing, int offset, int len)
            throws SaslException {
        if (completed) {
            throw new SaslException(
                    "EXTERNAL supports neither integrity nor privacy.");
        } else {
            throw new IllegalStateException(
                    "EXTERNAL authentication not completed.");
        }
    }

    /**
     * Retrieves the negotiated property. This method can be called only after
     * the authentication exchange has completed (i.e., when
     * <tt>isComplete()</tt> returns true); otherwise, a
     * <tt>IllegalStateException</tt> is thrown.
     * 
     * @return null No property is applicable to this mechanism.
     * @exception IllegalStateException if this authentication exchange has not
     * completed
     */
    public Object getNegotiatedProperty(String propName) {
        if (completed) {
            return null;
        } else {
            throw new IllegalStateException(
                    "EXTERNAL authentication not completed");
        }
    }

}
