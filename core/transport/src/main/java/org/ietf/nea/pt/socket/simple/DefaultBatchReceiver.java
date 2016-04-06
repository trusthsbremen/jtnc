/**
 * The BSD 3-Clause License ("BSD New" or "BSD Simplified")
 *
 * Copyright © 2015 Trust HS Bremen and its Contributors. All rights   
 * reserved.
 *
 * See the CONTRIBUTORS file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER
 * OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.ietf.nea.pt.socket.simple;

import java.util.Arrays;

import org.ietf.nea.pt.socket.Receiver;
import org.ietf.nea.pt.socket.SocketMessenger;
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

/**
 * Default message receiver for a transport connection.
 */
public class DefaultBatchReceiver implements Receiver {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(DefaultBatchReceiver.class);
    
    private PtTlsMessageValuePbBatch batch;

    @Override
    public ByteBuffer getTnccsData() {

        PtTlsMessageValuePbBatch retValue = this.batch;
        this.batch = null;
        return (retValue != null) ? retValue.getTnccsData() : null;
    }

    @Override
    public void receive(SocketMessenger wrappedSocket)
            throws SerializationException, ValidationException,
            ConnectionException {

        TransportMessage message = null;
        while (message == null) {
            
            try {
                message = wrappedSocket.readFromStream();
    
                if (message != null) {
                    if (message.getValue()
                            instanceof PtTlsMessageValuePbBatch) {
    
                        this.batch = (PtTlsMessageValuePbBatch)
                                message.getValue();
    
                    } else if (message.getValue()
                            instanceof PtTlsMessageValueError) {

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
                                        PtTlsMessageErrorCodeEnum
                                            .IETF_INVALID_MESSAGE.code(),
                                        PtTlsErrorCauseEnum
                                            .MESSAGE_TYPE_UNEXPECTED.id()),
                                0, message.getHeader());
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

    /**
     * Handles an error message, if one is received.
     * @param message the received error message
     * @throws ConnectionException if the contained error is fatal
     */
    private void handleErrorMessage(TransportMessage message)
            throws ConnectionException {
        PtTlsMessageValueError error = (PtTlsMessageValueError) message
                .getValue();

        if (error.getErrorVendorId()
                    == IETFConstants.IETF_PEN_VENDORID
                && error.getErrorCode()
                    == PtTlsMessageErrorCodeEnum
                        .IETF_UNSUPPORTED_MESSAGE_TYPE.code()) {
            
            LOGGER.warn("Peer responded with an error signaling an unsupported "
                    + "message type. This is not fatal. Message content: "
                    + Arrays.toString(error.getPartialMessageCopy()));
            
        } else {
            
            throw new ConnectionException(
                    "Error message received in negotiation phase: "
                            + error.toString()
                            + "\n Connection will be closed.");
        }
    }
}
