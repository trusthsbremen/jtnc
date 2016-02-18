package org.ietf.nea.pt.socket;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import org.ietf.nea.pt.message.PtTlsMessageFactoryIetf;
import org.ietf.nea.pt.validate.enums.PtTlsErrorCauseEnum;
import org.ietf.nea.pt.value.PtTlsMessageValueSaslMechanisms;
import org.ietf.nea.pt.value.enums.PtTlsMessageErrorCodeEnum;
import org.ietf.nea.pt.value.util.SaslMechanismEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.t.message.TransportMessage;
import de.hsbremen.tc.tnc.message.t.serialize.TransportMessageContainer;
import de.hsbremen.tc.tnc.transport.exception.ConnectionException;

public class SaslAuthenticator implements Authenticator {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(SaslAuthenticator.class);
    
    
    private final List<SaslMechanismEntry> supportedMechnisms;
    
    
    public SaslAuthenticator(List<SaslMechanismEntry> supportedMechnisms) {
        this.supportedMechnisms = supportedMechnisms != null ?
                supportedMechnisms : new ArrayList<SaslMechanismEntry>();
    }



    /* (non-Javadoc)
     * @see org.ietf.nea.pt.socket.Authenticator#authenticate()
     */
    @Override
    public void authenticate(AbstractSocketTransportConnection connection) throws ValidationException,
            ConnectionException, SerializationException {
        if (connection instanceof ServerSocketTransportConnection) {
            // empty for no extra authentication wanted and move on to transport
            // phase
            TransportMessage m = PtTlsMessageFactoryIetf
                    .createSaslMechanisms(connection.getIdentifier(),
                            this.supportedMechnisms.toArray(new SaslMechanismEntry[0]));

            connection.writeToStream(m);

        } else {

            TransportMessageContainer ct = null;
            while (ct == null) {
                try {
                    ct = connection.readFromStream();

                    if (ct != null
                            && ct.getResult() != null
                            && !(ct.getResult().getValue()
                                 instanceof PtTlsMessageValueSaslMechanisms)) {

                        throw new ValidationException(
                                "Unexpected message received.",
                                new RuleException(
                                        "Message of type "
                                                + ct.getResult().getValue()
                                                        .getClass()
                                                        .getCanonicalName()
                                                + " was not expected.",
                                        true,
                                        PtTlsMessageErrorCodeEnum.
                                        IETF_INVALID_MESSAGE.code(),
                                        PtTlsErrorCauseEnum.
                                        MESSAGE_TYPE_UNEXPECTED.id()),
                                        0, ct.getResult().getHeader());
                    }

                } catch (ValidationException e) {
                    if (e.getCause().isFatal()) {

                        throw e;

                    } else {

                        LOGGER.debug("Minor error occured. "
                                + "An error message will be send.");

                        ct = null;
                        TransportMessage m = connection.createValidationErrorMessage(e);

                        connection.writeToStream(m);
                    }
                }
            }

            if (((PtTlsMessageValueSaslMechanisms) ct.getResult().getValue())
                    .getMechanisms().size() > 0) {
                throw new ValidationException(
                        "Extra SASL authentication not supported.",
                        new RuleException(
                                "Extra SASL authentication not supported.",
                                true,
                                PtTlsMessageErrorCodeEnum.
                                IETF_SASL_MECHANISM_ERROR.code(),
                                PtTlsErrorCauseEnum.
                                ADDITIONAL_SASL_NOT_SUPPORTED.id()),
                                0, ct.getResult().getHeader());
            }
        }
    }
}
