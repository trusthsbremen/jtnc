package org.ietf.nea.pt.socket.simple;

import org.ietf.nea.pt.message.PtTlsMessageFactoryIetf;
import org.ietf.nea.pt.socket.Negotiator;
import org.ietf.nea.pt.socket.TransportMessenger;
import org.ietf.nea.pt.validate.enums.PtTlsErrorCauseEnum;
import org.ietf.nea.pt.value.PtTlsMessageValueError;
import org.ietf.nea.pt.value.PtTlsMessageValueVersionRequest;
import org.ietf.nea.pt.value.enums.PtTlsMessageErrorCodeEnum;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.t.message.TransportMessage;
import de.hsbremen.tc.tnc.transport.exception.ConnectionException;

public class DefaultNegotiationResponder implements Negotiator {

    private final short minVersion;
    private final short maxVersion;
    private final short prefVersion;
    private short negotiatedVersion;

    public DefaultNegotiationResponder(short minVersion, short maxVersion,
            short prefVersion) {
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

        TransportMessage message = null;

        while (message == null) {

            message = wrappedSocket.readFromStream();

            if (message != null) {
                if (message.getValue() instanceof PtTlsMessageValueVersionRequest) {

                    this.negotiatedVersion = this.chooseVersion(message);
                    if (this.negotiatedVersion > 0) {

                        TransportMessage m = PtTlsMessageFactoryIetf
                                .createVersionResponse(
                                        wrappedSocket.getIdentifier(),
                                        this.negotiatedVersion);
                        wrappedSocket.writeToStream(m);

                    } else {

                        throw new ValidationException(
                                "Version not supported.",
                                new RuleException(
                                        "Version "
                                                + prefVersion
                                                + " not in supported version range.",
                                        true,
                                        PtTlsMessageErrorCodeEnum.IETF_UNSUPPORTED_VERSION
                                                .code(),
                                        PtTlsErrorCauseEnum.TRANSPORT_VERSION_NOT_SUPPORTED
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
    }

    private short chooseVersion(TransportMessage message) {

        PtTlsMessageValueVersionRequest value = (PtTlsMessageValueVersionRequest) message
                .getValue();

        // if possible use prefered version from request
        if (minVersion <= value.getPreferredVersion()
                && maxVersion >= value.getPreferredVersion()) {
            return value.getPreferredVersion();

            // or look for highest version
        } else {

            // test out of range
            if (this.maxVersion < value.getMinVersion()
                    || this.minVersion > value.getMaxVersion()) {
                return 0;
            }

            // find max number in both range
            return (this.maxVersion > value.getMaxVersion()) ? value
                    .getMaxVersion()
                    : ((this.maxVersion == value.getMaxVersion()) ? value
                            .getMaxVersion() : this.maxVersion);
        }
    }

    private void handleErrorMessage(TransportMessage message)
            throws ConnectionException {
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
