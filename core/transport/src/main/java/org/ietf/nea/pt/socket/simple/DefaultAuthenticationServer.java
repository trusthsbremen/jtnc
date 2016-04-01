package org.ietf.nea.pt.socket.simple;

import java.util.HashSet;
import java.util.Set;

import javax.security.sasl.SaslException;
import javax.security.sasl.SaslServer;

import org.ietf.nea.pt.message.PtTlsMessageFactoryIetf;
import org.ietf.nea.pt.socket.Authenticator;
import org.ietf.nea.pt.socket.TransportMessenger;
import org.ietf.nea.pt.socket.enums.AuthenticatorTypeEnum;
import org.ietf.nea.pt.socket.sasl.SaslServerMechansims;
import org.ietf.nea.pt.validate.enums.PtTlsErrorCauseEnum;
import org.ietf.nea.pt.value.PtTlsMessageValueError;
import org.ietf.nea.pt.value.PtTlsMessageValueSaslAuthenticationData;
import org.ietf.nea.pt.value.PtTlsMessageValueSaslMechanismSelection;
import org.ietf.nea.pt.value.enums.PtTlsMessageErrorCodeEnum;
import org.ietf.nea.pt.value.enums.PtTlsSaslResultEnum;
import org.ietf.nea.pt.value.util.SaslMechanismEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.t.message.TransportMessage;
import de.hsbremen.tc.tnc.transport.exception.ConnectionException;

/**
 * Default authentication implementation for a NAA.
 * This implementation uses SASL for the authentication.
 * Because a SASL authentication mechanism can only be
 * executed once a new authenticator must  be added for every connection
 * that should be authenticated using this authenticator.
 * The authenticator is not reusable!
 */
public class DefaultAuthenticationServer implements Authenticator {
    
    private static final Logger LOGGER = LoggerFactory
            .getLogger(DefaultAuthenticationServer.class);
    
    private static final AuthenticatorTypeEnum TYPE =
            AuthenticatorTypeEnum.AUTH_SERVER;
    
    private final SaslServerMechansims mechanisms;
    private PtTlsSaslResultEnum result;
    
    /**
     * Creates a default server-side authenticator with no supported
     * SASL mechanisms.
     */
    public DefaultAuthenticationServer() {
        this(null);
    }
    
    /**
     * Creates a default server-side authenticator with a selection
     * of supported SASL server-side mechanisms.
     * All added SASL authentication mechanisms must not be completed or
     * the connection establishment will fail at a later point!
     * 
     * @param mechanisms the supported SASL server-side mechanisms
     */
    public DefaultAuthenticationServer(
            SaslServerMechansims mechanisms) {
        
        this.mechanisms = (mechanisms != null) ? mechanisms
                : new SaslServerMechansims();
        this.result = null;
    }

    @Override
    public AuthenticatorTypeEnum getType() {
        return TYPE;
    }

    
    @Override
    public void authenticate(TransportMessenger connection)
            throws ValidationException, ConnectionException,
            SerializationException {
        
        byte mechStages = this.mechanisms.getStageCount();
        try {
            for (byte i = 1; i <= mechStages; i++) {
                
                if (this.mechanisms.getStageMechanismCount(i) > 0) {
                    LOGGER.debug("Authentication mechanisms found: "
                            + this.mechanisms
                                .getAllMechanismsByStage(i).toString()
                            + "\n Negotiate mechanism.");
                    
                    PtTlsMessageValueSaslMechanismSelection value = 
                            this.negotiateMechanisms(i, connection);
        
                    LOGGER.debug("Authentication mechanisms choosen: "
                            + value.toString()
                            + "\n Start Authentication.");
                    
                    this.executeAuthentication(
                            this.mechanisms.getMechanismByStage(i,
                                    value.getMechanism().getName()),
                            value.getInitialSaslMsg(), connection);
                }
            }

            TransportMessage m = PtTlsMessageFactoryIetf
                    .createSaslMechanisms(connection.getIdentifier());
    
            connection.writeToStream(m);
            
            result = PtTlsSaslResultEnum.SUCCESS;
            
        } catch (SaslException e) {
            
            // if on authentication fails everything is 
            // shut down in this implementation. 
            // This could be changed by moving this up
            // into the for loop and not throwing a connection
            // exception.
            
            TransportMessage m = null;
            
            StringBuilder sb = new StringBuilder();
            sb.append("SASL Authentication failed: ");
            
            if (e.getCause() != null) {
                result = PtTlsSaslResultEnum.MECHANISM_FAILURE;
                sb.append(result.toString());
                m = PtTlsMessageFactoryIetf
                        .createSaslResult(connection.getIdentifier(),
                                PtTlsSaslResultEnum.MECHANISM_FAILURE);
                
            } else {
                result = PtTlsSaslResultEnum.FAILURE;
                sb.append(result.toString());
                m = PtTlsMessageFactoryIetf
                .createSaslResult(connection.getIdentifier(), result);
            }

            connection.writeToStream(m);
            
            throw new ConnectionException(sb.toString(), e, result);
        }
    }

    @Override
    public PtTlsSaslResultEnum getAuthenticationResult() {
        return this.result;
    }
    
    /**
     * Executes the server-side mechanism negotiation protocol flow.
     * 
     * @param stage the current authentication stage
     * @param wrappedSocket the message handler containing the socket
     * for message transmission
     * 
     * @return a list of the names for suitable mechanisms
     * @throws SerializationException if an error occurs at message
     * serialization
     * @throws ValidationException if an error occurs at message parsing
     * @throws ConnectionException if the connection is broken
     */
    private PtTlsMessageValueSaslMechanismSelection negotiateMechanisms(
            byte stage, TransportMessenger wrappedSocket)
                    throws ValidationException,
                    ConnectionException,
                    SerializationException {
        
        Set<String> supportedMechanismNames = this.mechanisms
                .getAllMechanismsByStage(stage).keySet();
        
        Set<SaslMechanismEntry> supportedMechanisms = new HashSet<>();
        for (String mechanismName : supportedMechanismNames) {
            supportedMechanisms.add(new SaslMechanismEntry(mechanismName));
        }
       
        TransportMessage m = PtTlsMessageFactoryIetf
                .createSaslMechanisms(wrappedSocket.getIdentifier(),
                        supportedMechanisms.toArray(new SaslMechanismEntry[0]));
        wrappedSocket.writeToStream(m);
        
        TransportMessage message = null;
        while (message == null) {

            message = wrappedSocket.readFromStream();

            if (message != null) {
                if (message.getValue()
                        instanceof PtTlsMessageValueSaslMechanismSelection) {

                    SaslMechanismEntry mechanismEntry =
                            ((PtTlsMessageValueSaslMechanismSelection)
                            message.getValue()).getMechanism();
                    
                    if (!supportedMechanisms.contains(mechanismEntry)) {

                        throw new ValidationException(
                                "Unacceptable SASL mechanism choosen.",
                                new RuleException(
                                        "The mechanism "
                                                + mechanismEntry.toString()
                                                + " is not supported.",
                                        true,
                                        PtTlsMessageErrorCodeEnum
                                            .IETF_SASL_MECHANISM_ERROR.code(),
                                        PtTlsErrorCauseEnum
                                            .SASL_NAMING_MISMATCH.id()),
                                            16 + 1,
                                        message.getHeader());
                    }

                } else if (message.getValue()
                        instanceof PtTlsMessageValueError) {

                    this.handleErrorMessage(message);

                } else {

                    throw new ValidationException(
                            "Unexpected message received.",
                            new RuleException(
                                    "Message of type "
                                            + message.getValue().getClass()
                                                    .getCanonicalName()
                                            + " was not expected.",
                                    true,
                                    PtTlsMessageErrorCodeEnum
                                        .IETF_INVALID_MESSAGE.code(),
                                    PtTlsErrorCauseEnum
                                        .MESSAGE_TYPE_UNEXPECTED.id()),
                                        0, message.getHeader());
                }
            }

            // Usually exceptions would be distinguished at the point.
            // In this case throw everything although there are minor errors,
            // Minor error should not happen here.
        }
        
        return (PtTlsMessageValueSaslMechanismSelection) message.getValue();
    }
    
    /**
     * Executes the server-side authentication protocol flow using a chosen
     * SASL mechanism.
     * 
     * @param saslServer the chosen SASL server-side
     * mechanism to use for the authentication
     * @param initialMessage an optional initial message provided by the client
     * @param wrappedSocket the message handler containing the socket
     * for message transmission
     * 
     * @throws SerializationException if an error occurs at message
     * serialization
     * @throws ValidationException if an error occurs at message parsing
     * @throws ConnectionException if the connection is broken
     * @throws SaslException if SASL authentication fails
     */
    private void executeAuthentication(SaslServer saslServer,
            byte[] initialMessage, TransportMessenger wrappedSocket)
            throws ValidationException, ConnectionException,
            SerializationException, SaslException {

        byte[] clientResponse = initialMessage;

        if (clientResponse == null || clientResponse.length <= 0) {

            TransportMessage m = PtTlsMessageFactoryIetf
                    .createSaslAuthenticationData(
                            wrappedSocket.getIdentifier(), new byte[0]);
            wrappedSocket.writeToStream(m);

            TransportMessage message = null;
            while (message == null) {

                message = wrappedSocket.readFromStream();

                if (message != null) {
                    if (message.getValue()
                            instanceof PtTlsMessageValueSaslAuthenticationData)
                    {

                        clientResponse =
                                ((PtTlsMessageValueSaslAuthenticationData)
                                message.getValue()).getAuthenticationData();

                    } else if (message.getValue()
                            instanceof PtTlsMessageValueError) {

                        this.handleErrorMessage(message);

                    } else {
                        throw new ValidationException(
                                "Unexpected message received.",
                                new RuleException(
                                        "Message of type "
                                                + message.getValue().getClass()
                                                        .getCanonicalName()
                                                + " was not expected.",
                                        true,
                                        PtTlsMessageErrorCodeEnum
                                            .IETF_INVALID_MESSAGE.code(),
                                        PtTlsErrorCauseEnum
                                            .MESSAGE_TYPE_UNEXPECTED.id()),
                                            0, message.getHeader());
                    }
                }

                // Usually exceptions would be distinguished at the point.
                // In this case throw everything although there are minor
                // errors,
                // Minor error should not happen here.
            }
        }
        
        while (!saslServer.isComplete()) {

            byte[] challenge = saslServer.evaluateResponse(clientResponse);

            if (saslServer.isComplete()) {

                TransportMessage m = PtTlsMessageFactoryIetf.createSaslResult(
                        wrappedSocket.getIdentifier(),
                        PtTlsSaslResultEnum.SUCCESS);
                wrappedSocket.writeToStream(m);

            } else {

                TransportMessage m = PtTlsMessageFactoryIetf
                        .createSaslAuthenticationData(
                                wrappedSocket.getIdentifier(),
                                (challenge != null) ? challenge : new byte[0]);

                wrappedSocket.writeToStream(m);

                TransportMessage message = null;
                while (message == null) {

                    message = wrappedSocket.readFromStream();

                    if (message != null) {
                        if (message.getValue()instanceof
                                PtTlsMessageValueSaslAuthenticationData) {

                            clientResponse =
                                    ((PtTlsMessageValueSaslAuthenticationData)
                                            message.getValue())
                                            .getAuthenticationData();

                        } else if (message.getValue()
                                instanceof PtTlsMessageValueError) {
                            this.handleErrorMessage(message);

                        } else {
                            throw new ValidationException(
                                    "Unexpected message received.",
                                    new RuleException(
                                            "Message of type "
                                                    + message.getValue()
                                                            .getClass()
                                                            .getCanonicalName()
                                                    + " was not expected.",
                                            true,
                                            PtTlsMessageErrorCodeEnum
                                                .IETF_INVALID_MESSAGE.code(),
                                            PtTlsErrorCauseEnum
                                                .MESSAGE_TYPE_UNEXPECTED.id()),
                                                0,
                                            message.getHeader());
                        }
                    }

                    // Usually exceptions would be distinguished at the point.
                    // In this case throw everything although there are minor
                    // errors,
                    // Minor error should not happen here.
                }
            }
        }
    }
    
    /**
     * Handles an error message, if one is received.
     * @param message the received error message
     * @throws ConnectionException if the contained error is fatal
     */
    private void handleErrorMessage(TransportMessage message)
            throws ConnectionException {
        PtTlsMessageValueError error = (PtTlsMessageValueError) message
                .getValue();
        
        this.result = PtTlsSaslResultEnum.ABORT;
        // An error violates the protocol flow.
        // In this regard even a minor error must
        // lead to a connection break up. 
        throw new ConnectionException(
                "Error message received in authentication phase: "
                        + error.toString() + "\n Connection will be closed.");

    }
}
