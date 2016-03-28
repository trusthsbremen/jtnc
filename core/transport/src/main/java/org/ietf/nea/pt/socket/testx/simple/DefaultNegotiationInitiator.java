package org.ietf.nea.pt.socket.testx.simple;

import org.ietf.nea.pt.message.PtTlsMessageFactoryIetf;
import org.ietf.nea.pt.socket.testx.Negotiator;
import org.ietf.nea.pt.socket.testx.TransportMessenger;
import org.ietf.nea.pt.validate.enums.PtTlsErrorCauseEnum;
import org.ietf.nea.pt.value.PtTlsMessageValueError;
import org.ietf.nea.pt.value.PtTlsMessageValueVersionResponse;
import org.ietf.nea.pt.value.enums.PtTlsMessageErrorCodeEnum;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.t.message.TransportMessage;
import de.hsbremen.tc.tnc.transport.exception.ConnectionException;

public class DefaultNegotiationInitiator implements Negotiator {
    
    private final short minVersion;
    private final short maxVersion;
    private final short prefVersion;
    private short negotiatedVersion;

    public DefaultNegotiationInitiator(short minVersion, short maxVersion, short prefVersion) {
        this.minVersion = minVersion;
        this.maxVersion = maxVersion;
        this.prefVersion = prefVersion;
        this.negotiatedVersion = 0;
    }


    @Override
    public short getNegotiatedVersion() {
        return this.negotiatedVersion;
    }
    
    
    @Override
    public void negotiate(TransportMessenger wrappedSocket)
            throws SerializationException, ValidationException,
            ConnectionException {

        TransportMessage m = PtTlsMessageFactoryIetf.createVersionRequest(
                wrappedSocket.getIdentifier(), minVersion, maxVersion,
                prefVersion);
        wrappedSocket.writeToStream(m);

        TransportMessage message = null;
        while (message == null) {

            message = wrappedSocket.readFromStream();

            if (message != null) {
                if (message.getValue() instanceof PtTlsMessageValueVersionResponse) {

                    this.handleVersionResponseMessage(message);

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
    }

    private void handleVersionResponseMessage(TransportMessage message) throws ValidationException {
        
        PtTlsMessageValueVersionResponse value = (PtTlsMessageValueVersionResponse) message
                .getValue();

        if (minVersion > value.getSelectedVersion() ||  maxVersion < value
                .getSelectedVersion()) {

            throw new ValidationException("Version not supported.",
                    new RuleException("Version "
                            + ((PtTlsMessageValueVersionResponse) message
                                    .getValue()).getSelectedVersion()
                            + " not in supported version range.", true,
                            PtTlsMessageErrorCodeEnum.IETF_UNSUPPORTED_VERSION
                                    .code(),
                            PtTlsErrorCauseEnum.TRANSPORT_VERSION_NOT_SUPPORTED
                                    .id()), 16+3, message.getHeader());
        }
        
        this.negotiatedVersion = value.getSelectedVersion();
    }
    
    private void handleErrorMessage(TransportMessage message) throws ConnectionException {
        PtTlsMessageValueError error = (PtTlsMessageValueError) message
                .getValue();

        // An error violates the protocol flow.
        // In this regard even a minor error must
        // lead to a connection break up. 
        throw new ConnectionException(
                "Error message received in negotiation phase: "
                        + error.toString() + "\n Connection will be closed.");
    }
}
