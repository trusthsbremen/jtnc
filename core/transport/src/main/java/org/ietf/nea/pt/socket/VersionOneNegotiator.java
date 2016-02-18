package org.ietf.nea.pt.socket;

import org.ietf.nea.pt.message.PtTlsMessageFactoryIetf;
import org.ietf.nea.pt.validate.enums.PtTlsErrorCauseEnum;
import org.ietf.nea.pt.value.PtTlsMessageValueVersionRequest;
import org.ietf.nea.pt.value.PtTlsMessageValueVersionResponse;
import org.ietf.nea.pt.value.enums.PtTlsMessageErrorCodeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.t.message.TransportMessage;
import de.hsbremen.tc.tnc.message.t.serialize.TransportMessageContainer;
import de.hsbremen.tc.tnc.transport.exception.ConnectionException;

public class VersionOneNegotiator implements VersionNegotiator {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(VersionOneNegotiator.class);
    
    private static final short MIN_VERSION = 1;
    private static final short MAX_VERSION = 1;
    private static final short PREF_VERSION = 1;
    

    /* (non-Javadoc)
     * @see org.ietf.nea.pt.socket.VersionNegotiator#negotiateVersion()
     */
    @Override
    public void negotiate(AbstractSocketTransportConnection connection) throws SerializationException,
            ValidationException, ConnectionException {
        
        if (connection.isSelfInititated()) {
            TransportMessage m = PtTlsMessageFactoryIetf.createVersionRequest(
                    connection.getIdentifier(), MIN_VERSION, MAX_VERSION,
                    PREF_VERSION);
            connection.writeToStream(m);

            TransportMessageContainer ct = null;
            while (ct == null) {
                try {

                    ct = connection.readFromStream();

                    if (ct != null
                            && ct.getResult() != null
                            && !(ct.getResult().getValue()
                                instanceof PtTlsMessageValueVersionResponse)) {

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

                        ct = null;
                        LOGGER.debug("Minor error occured. "
                                + "An error message will be send.");
                        m = connection.createValidationErrorMessage(e);
                        connection.writeToStream(m);
                    }
                }
            }

            if (!(PREF_VERSION >= ((PtTlsMessageValueVersionResponse)
                    ct.getResult().getValue()).getSelectedVersion()
                    && PREF_VERSION >= ((PtTlsMessageValueVersionResponse)
                    ct.getResult().getValue()).getSelectedVersion())) {

                throw new ValidationException(
                        "Version not supported.",
                        new RuleException(
                                "Version "
                                        + ((PtTlsMessageValueVersionResponse) ct
                                                .getResult().getValue())
                                                .getSelectedVersion()
                                        + " not in supported version range.",
                                true,
                                PtTlsMessageErrorCodeEnum.
                                IETF_UNSUPPORTED_VERSION.code(),
                                PtTlsErrorCauseEnum.TRANSPORT_VERSION_NOT_SUPPORTED.id()),
                                0, ct.getResult().getHeader());
            }

        } else {

            TransportMessageContainer ct = null;

            while (ct == null) {
                try {

                    ct = connection.readFromStream();

                    if (ct != null
                            && ct.getResult() != null
                            && !(ct.getResult().getValue()
                                  instanceof PtTlsMessageValueVersionRequest)) {

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

                        LOGGER.debug("Minor error occured."
                                + " An error message will be send.");
                        ct = null;
                        TransportMessage m = connection.createValidationErrorMessage(e);
                        connection.writeToStream(m);
                    }
                }
            }

            if (PREF_VERSION >= ((PtTlsMessageValueVersionRequest) ct
                    .getResult().getValue()).getMinVersion()
                    && PREF_VERSION >= ((PtTlsMessageValueVersionRequest) ct
                            .getResult().getValue()).getMaxVersion()) {

                TransportMessage m = PtTlsMessageFactoryIetf
                        .createVersionResponse(connection.getIdentifier(),
                                PREF_VERSION);

                connection.writeToStream(m);

            } else {

                throw new ValidationException(
                        "Version not supported.",
                        new RuleException(
                                "Version " + PREF_VERSION
                                        + " not in supported version range.",
                                true,
                                PtTlsMessageErrorCodeEnum.
                                IETF_UNSUPPORTED_VERSION.code(),
                                PtTlsErrorCauseEnum.TRANSPORT_VERSION_NOT_SUPPORTED.id()),
                                0, ct.getResult().getHeader());
            }
        }
    }
}
