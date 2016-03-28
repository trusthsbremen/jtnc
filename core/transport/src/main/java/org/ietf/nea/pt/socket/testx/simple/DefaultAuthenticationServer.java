package org.ietf.nea.pt.socket.testx.simple;

import java.util.HashSet;
import java.util.Set;

import javax.security.sasl.SaslException;
import javax.security.sasl.SaslServer;

import org.ietf.nea.pt.message.PtTlsMessageFactoryIetf;
import org.ietf.nea.pt.socket.sasl.SaslServerMechansims;
import org.ietf.nea.pt.socket.testx.Authenticator;
import org.ietf.nea.pt.socket.testx.TransportMessenger;
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

public class DefaultAuthenticationServer implements Authenticator {
    
    private static final Logger LOGGER = LoggerFactory
            .getLogger(DefaultAuthenticationServer.class);
    
    private final SaslServerMechansims mechanisms;
    private PtTlsSaslResultEnum result;
    
    public DefaultAuthenticationServer(
            SaslServerMechansims mechanisms) {
        
        this.mechanisms = (mechanisms != null) ? mechanisms
                : new SaslServerMechansims();
        this.result = null;
    }

    @Override
    public void authenticate(TransportMessenger connection)
            throws ValidationException, ConnectionException,
            SerializationException {
        
        byte mechStages = this.mechanisms.getStageCount();
        try{
            for (byte i = 1; i <= mechStages; i++) {
                if(this.mechanisms.getStageMechanismCount(i) > 0){
                    LOGGER.debug("Authentication mechanisms found: "
                            + this.mechanisms.getAllMechanismsByStage(i).toString()
                            + "\n Negotiate mechanism.");
                    
                    PtTlsMessageValueSaslMechanismSelection value = 
                            this.negotiateMechanisms(
                                    this.mechanisms.getAllMechanismsByStage(i).keySet(),
                                    connection);
        
                    LOGGER.debug("Authentication mechanisms choosen: " + value.toString()
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
            
        }catch(SaslException e){
            
            // if on authentication fails everything is 
            // shut down in this implementation. 
            // This could be changed by moving this up
            // into the for loop and not throwing a connection
            // exception.
            
            TransportMessage m = null;
            
            StringBuilder sb = new StringBuilder();
            sb.append("SASL Authentication failed: ");
            
            if(e.getCause() != null){
                result = PtTlsSaslResultEnum.MECHANISM_FAILURE;
                sb.append(result.toString());
                m = PtTlsMessageFactoryIetf
                        .createSaslResult(connection.getIdentifier(),
                                PtTlsSaslResultEnum.MECHANISM_FAILURE);
                
            }else{
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
    public PtTlsSaslResultEnum getAuthenticationResult(){
        return this.result;
    }
    
    private PtTlsMessageValueSaslMechanismSelection negotiateMechanisms(Set<String> mechanismNames,
            TransportMessenger wrappedSocket)  throws ValidationException,
            ConnectionException,
            SerializationException {
        
        
        Set<SaslMechanismEntry> supportedMechanisms = new HashSet<>();
        
        for (String mechanismName : mechanismNames) {
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
                if (message.getValue() instanceof PtTlsMessageValueSaslMechanismSelection) {

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
                                        PtTlsMessageErrorCodeEnum.IETF_SASL_MECHANISM_ERROR
                                                .code(),
                                        PtTlsErrorCauseEnum.SASL_NAMING_MISMATCH
                                                .id()), 16 + 1,
                                        message.getHeader());
                    }

                } else if (message.getValue() instanceof PtTlsMessageValueError) {
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
                                    PtTlsMessageErrorCodeEnum.IETF_INVALID_MESSAGE
                                            .code(),
                                    PtTlsErrorCauseEnum.MESSAGE_TYPE_UNEXPECTED
                                            .id()), 0, message.getHeader());
                }
            }

            // Usually exceptions would be distinguished at the point.
            // In this case throw everything although there are minor errors,
            // Minor error should not happen here.
        }
        
        return (PtTlsMessageValueSaslMechanismSelection)message.getValue();
    }
    
    private void executeAuthentication(SaslServer server,
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
                    if (message.getValue() instanceof PtTlsMessageValueSaslAuthenticationData) {

                        clientResponse = ((PtTlsMessageValueSaslAuthenticationData) message
                                .getValue()).getAuthenticationData();

                    } else if (message.getValue() instanceof PtTlsMessageValueError) {
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
                                        PtTlsMessageErrorCodeEnum.IETF_INVALID_MESSAGE
                                                .code(),
                                        PtTlsErrorCauseEnum.MESSAGE_TYPE_UNEXPECTED
                                                .id()), 0, message.getHeader());
                    }
                }

                // Usually exceptions would be distinguished at the point.
                // In this case throw everything although there are minor
                // errors,
                // Minor error should not happen here.
            }
        }
        while (!server.isComplete()) {

            byte[] challenge = server.evaluateResponse(clientResponse);

            if (server.isComplete()) {

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
                        if (message.getValue() instanceof PtTlsMessageValueSaslAuthenticationData) {

                            clientResponse = ((PtTlsMessageValueSaslAuthenticationData) message
                                    .getValue()).getAuthenticationData();

                        } else if (message.getValue() instanceof PtTlsMessageValueError) {
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
                                            PtTlsMessageErrorCodeEnum.IETF_INVALID_MESSAGE
                                                    .code(),
                                            PtTlsErrorCauseEnum.MESSAGE_TYPE_UNEXPECTED
                                                    .id()), 0,
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
    
    private void handleErrorMessage(TransportMessage message) throws ConnectionException {
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
