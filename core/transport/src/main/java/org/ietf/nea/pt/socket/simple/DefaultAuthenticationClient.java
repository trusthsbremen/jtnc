package org.ietf.nea.pt.socket.simple;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.security.sasl.SaslClient;
import javax.security.sasl.SaslException;

import org.ietf.nea.pt.message.PtTlsMessageFactoryIetf;
import org.ietf.nea.pt.socket.Authenticator;
import org.ietf.nea.pt.socket.TransportMessenger;
import org.ietf.nea.pt.socket.enums.AuthenticatorTypeEnum;
import org.ietf.nea.pt.socket.sasl.SaslClientMechansims;
import org.ietf.nea.pt.validate.enums.PtTlsErrorCauseEnum;
import org.ietf.nea.pt.value.PtTlsMessageValueError;
import org.ietf.nea.pt.value.PtTlsMessageValueSaslAuthenticationData;
import org.ietf.nea.pt.value.PtTlsMessageValueSaslMechanisms;
import org.ietf.nea.pt.value.PtTlsMessageValueSaslResult;
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
 * Default authentication implementation for a NAR.
 * This implementation uses SASL for the authentication.
 */
public class DefaultAuthenticationClient implements Authenticator {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(DefaultAuthenticationClient.class);

    private static final AuthenticatorTypeEnum TYPE =
            AuthenticatorTypeEnum.AUTH_CLIENT;
    
    private final SaslClientMechansims mechanisms;
    
    private PtTlsSaslResultEnum result;
    
    /**
     * Creates a default client-side authenticator with no supported
     * SASL mechanisms.
     */
    public DefaultAuthenticationClient() {
        this(null);
    }
    
    /**
     * Creates a default client-side authenticator with a selection
     * of supported SASL client-side mechanisms.
     * 
     * @param mechanisms the supported SASL client-side mechanisms
     */
    public DefaultAuthenticationClient(
            SaslClientMechansims mechanisms) {
        this.mechanisms = (mechanisms != null) ? mechanisms
                : new SaslClientMechansims(); 
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
        
        List<SaslMechanismEntry> suitableMechanisms = null;
        try {
            
            suitableMechanisms =
                    this.negotiateMechanisms(connection);
           
            while (suitableMechanisms != null
                    && !suitableMechanisms.isEmpty()) {

                LOGGER.debug("Suitable Mechanisms found: "
                        + suitableMechanisms.toString()
                        + "\n Start Authentication.");
                
                //TODO make better mechanism selection,
                // then by position in list and
                // mechanism state "not completed".
                
                Iterator<SaslMechanismEntry> iter =
                        suitableMechanisms.iterator();
                
                SaslClient sc = null;
                while (iter.hasNext() && sc == null) {
                    
                    sc = this.mechanisms.getMechanism(
                        suitableMechanisms.iterator().next().getName());
                    if (sc.isComplete()) {
                        sc = null;
                    }
                }
                
                if (sc == null) {
                    throw new ValidationException(
                            "Unacceptable SASL mechanism.",
                            new RuleException(
                                    "The collection of mechanisms "
                                            + mechanisms.toString()
                                            + " is not supported.",
                                    true,
                                    PtTlsMessageErrorCodeEnum
                                        .IETF_INVALID_MESSAGE.code(),
                                    PtTlsErrorCauseEnum.SASL_NAMING_MISMATCH
                                            .id()), 0);
                }

                PtTlsSaslResultEnum localResult =
                        this.executeAuthentication(sc, connection);
                
                switch (localResult) {
                    case SUCCESS:
                        // everything ok, do nothing
                        break;
                    default:
                        throw new ConnectionException(
                                "Authentication failed with reason: "
                           + localResult.toString(), localResult);
                }
                
                suitableMechanisms =
                        this.negotiateMechanisms(connection);
                
            }
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
       
        this.result = PtTlsSaslResultEnum.SUCCESS;
        
    }

    @Override
    public PtTlsSaslResultEnum getAuthenticationResult() {
        return this.result;
    }
    
    /**
     * Executes the client-side mechanism negotiation protocol flow.
     * 
     * @param wrappedSocket the message handler containing the socket
     * for message transmission
     * 
     * @return a list of the names for suitable mechanisms
     * @throws SerializationException if an error occurs at message
     * serialization
     * @throws ValidationException if an error occurs at message parsing
     * @throws ConnectionException if the connection is broken
     */
    private List<SaslMechanismEntry> negotiateMechanisms(
            TransportMessenger wrappedSocket)
                    throws ValidationException,
                    ConnectionException,
                    SerializationException {
        
        Set<String> supportedMechanismNames =
                this.mechanisms.getAllMechansims().keySet();
        
        List<SaslMechanismEntry> supportedMechanisms = new ArrayList<>();
        for (String mechanismName : supportedMechanismNames) {
            supportedMechanisms.add(new SaslMechanismEntry(mechanismName));
        }
        
        List<SaslMechanismEntry> suitableMechanism = new ArrayList<>();
        
        TransportMessage message = null;
        while (message == null) {

            message = wrappedSocket.readFromStream();

            if (message != null) {
                if (message.getValue()
                        instanceof PtTlsMessageValueSaslMechanisms) {

                    List<SaslMechanismEntry> mechanismEntries =
                            ((PtTlsMessageValueSaslMechanisms)
                            message.getValue()).getMechanisms();
                    
                    if (mechanismEntries != null
                            && !mechanismEntries.isEmpty()) {
                        
                        List<SaslMechanismEntry> filteredList =
                                new ArrayList<>(supportedMechanisms);
                                
                        filteredList.retainAll(mechanismEntries);
                        
                        if (filteredList.isEmpty()) {
    
                            throw new ValidationException(
                                    "Unacceptable SASL mechanism.",
                                    new RuleException(
                                            "The collection of mechanisms "
                                                    + mechanismEntries
                                                        .toString()
                                                    + " is not supported.",
                                            true,
                                            PtTlsMessageErrorCodeEnum
                                                .IETF_INVALID_MESSAGE.code(),
                                            PtTlsErrorCauseEnum
                                                .SASL_NAMING_MISMATCH.id()),
                                                16,
                                            message.getHeader());
                        }
                    
                        suitableMechanism = filteredList;
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
                                    PtTlsErrorCauseEnum.MESSAGE_TYPE_UNEXPECTED
                                            .id()), 0, message.getHeader());
                }
            }

            // Usually exceptions would be distinguished at the point.
            // In this case throw everything although there are minor errors,
            // Minor error should not happen here.
        }
        
        return suitableMechanism;
       
    }
    
    /**
     * Executes the client-side authentication protocol flow using a chosen
     * SASL mechanism.
     * 
     * @param saslClient the chosen SASL client-side
     * mechanism to use for the authentication
     * @param wrappedSocket the message handler containing the socket
     * for message transmission
     * 
     * @return the authentication result (e.g. SUCCESS)
     * @throws SerializationException if an error occurs at message
     * serialization
     * @throws ValidationException if an error occurs at message parsing
     * @throws ConnectionException if the connection is broken
     * @throws SaslException if SASL authentication fails
     */
    private PtTlsSaslResultEnum executeAuthentication(SaslClient saslClient,
            TransportMessenger wrappedSocket)
                    throws ValidationException,
                    ConnectionException,
                    SerializationException, SaslException {
        
        PtTlsSaslResultEnum lokalResult = null;
        
        TransportMessage m = null;
        if (saslClient.hasInitialResponse()) {
            byte[] response = saslClient.evaluateChallenge(new byte[0]);
            m = PtTlsMessageFactoryIetf
                    .createSaslMechanismSelection(wrappedSocket.getIdentifier(),
                            new SaslMechanismEntry(
                                    saslClient.getMechanismName()), response);
        } else {
            m = PtTlsMessageFactoryIetf
                    .createSaslMechanismSelection(wrappedSocket.getIdentifier(),
                            new SaslMechanismEntry(
                                    saslClient.getMechanismName()));
        }
        
        wrappedSocket.writeToStream(m);
        
        while (!saslClient.isComplete()) {
            
            TransportMessage message = null;
            while (message == null) {
    
                message = wrappedSocket.readFromStream();
    
                if (message != null) {
                    if (message.getValue()
                            instanceof PtTlsMessageValueSaslAuthenticationData)
                    {
    
                        byte[] serverChallenge =
                                ((PtTlsMessageValueSaslAuthenticationData)
                                        message.getValue())
                                        .getAuthenticationData();
                        
                        byte[] response = saslClient
                                .evaluateChallenge(serverChallenge);
                        
                        message = PtTlsMessageFactoryIetf
                                .createSaslAuthenticationData(
                                        wrappedSocket.getIdentifier(),
                                        response);
                        
                        wrappedSocket.writeToStream(m);
    
                    } else if (message.getValue()
                            instanceof PtTlsMessageValueSaslResult) {
                        
                        PtTlsMessageValueSaslResult value =
                                (PtTlsMessageValueSaslResult)
                                message.getValue();
                        
                        lokalResult = value.getResult();
                        
                        byte[] resultData = value.getResultData();
                        
                        if (resultData != null && resultData.length > 0) {
                            LOGGER.warn("SASL result contained additional data."
                                    + " This  data is ignored by "
                                    + " this implementation.");
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
                // In this case throw everything although there are minor
                // errors,
                // Minor error should not happen here.
            }
 
        }
        
        if (lokalResult == null) {
            TransportMessage message = null;
            while (message == null) {
    
                message = wrappedSocket.readFromStream();
    
                if (message != null) {
                    if (message.getValue()
                            instanceof PtTlsMessageValueSaslResult) {
                        
                        PtTlsMessageValueSaslResult value =
                                (PtTlsMessageValueSaslResult)
                                message.getValue();
                        
                        lokalResult = value.getResult();
                        
                        byte[] resultData = value.getResultData();
                        
                        if (resultData != null && resultData.length > 0) {
                            
                            LOGGER.warn("SASL result contained additional data."
                                    + " This  data is ignored by this"
                                    + " implementation.");
                        
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
                // In this case throw everything although there are minor
                // errors,
                // Minor error should not happen here.
            }
        }
       
        return lokalResult;
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
