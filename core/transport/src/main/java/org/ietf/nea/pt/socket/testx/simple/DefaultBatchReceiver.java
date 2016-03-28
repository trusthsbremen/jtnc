package org.ietf.nea.pt.socket.testx.simple;

import java.util.Arrays;

import org.ietf.nea.pt.socket.testx.Receiver;
import org.ietf.nea.pt.socket.testx.TransportMessenger;
import org.ietf.nea.pt.validate.enums.PtTlsErrorCauseEnum;
import org.ietf.nea.pt.value.PtTlsMessageValueError;
import org.ietf.nea.pt.value.PtTlsMessageValuePbBatch;
import org.ietf.nea.pt.value.enums.PtTlsMessageErrorCodeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.t.message.TransportMessage;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.transport.exception.ConnectionException;

public class DefaultBatchReceiver implements Receiver {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(DefaultBatchReceiver.class);
    
    private PtTlsMessageValuePbBatch batch;

    /* (non-Javadoc)
     * @see org.ietf.nea.pt.socket.testx.Transporter#getTnccsData()
     */
    @Override
    public ByteBuffer getTnccsData() {

        PtTlsMessageValuePbBatch batch = this.batch;
        this.batch = null;
        return (batch != null) ? batch.getTnccsData() : null;
    }

    /* (non-Javadoc)
     * @see org.ietf.nea.pt.socket.testx.Transporter#transport(org.ietf.nea.pt.socket.testx.TransportMessenger)
     */
    @Override
    public void receive(TransportMessenger wrappedSocket)
            throws SerializationException, ValidationException,
            ConnectionException {

        TransportMessage message = null;
        while (message == null) {
            
            try{
                message = wrappedSocket.readFromStream();
    
                if (message != null) {
                    if (message.getValue() instanceof PtTlsMessageValuePbBatch) {
    
                        this.batch = (PtTlsMessageValuePbBatch) message.getValue();
    
                    } else if (message.getValue() instanceof PtTlsMessageValueError) {
                        this.handleErrorMessage(message);
                        message = null;
    
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

            } catch (ValidationException e) {

                if (e.getCause().isFatal()) {

                    throw e;

                } else {

                    LOGGER.warn("Minor error occured. "
                            + "An error message will be send.", e);
                    
                    message = null;
                    
                    TransportMessage m =
                            wrappedSocket.createValidationErrorMessage(e);

                    wrappedSocket.writeToStream(m);

                }
            }
        }
    }

    private void handleErrorMessage(TransportMessage message)
            throws ConnectionException {
        PtTlsMessageValueError error = (PtTlsMessageValueError) message
                .getValue();

        if(error.getErrorVendorId() == IETFConstants.IETF_PEN_VENDORID
                && error.getErrorCode() ==
                PtTlsMessageErrorCodeEnum.IETF_UNSUPPORTED_MESSAGE_TYPE.code()){
            
            LOGGER.warn("Peer responded with an error signaling an unsupported "
                    + "message type. This is not fatal. Message content: " +
                    Arrays.toString(error.getPartialMessageCopy()));
            
            
        }else{
            
            throw new ConnectionException(
                    "Error message received in negotiation phase: "
                            + error.toString() + "\n Connection will be closed.");
        }
    }
}
