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

import org.ietf.nea.pt.message.PtTlsMessageFactoryIetf;
import org.ietf.nea.pt.socket.Negotiator;
import org.ietf.nea.pt.socket.SocketMessenger;
import org.ietf.nea.pt.socket.enums.NegotiatorTypeEnum;
import org.ietf.nea.pt.validate.enums.PtTlsErrorCauseEnum;
import org.ietf.nea.pt.value.PtTlsMessageValueError;
import org.ietf.nea.pt.value.PtTlsMessageValueVersionRequest;
import org.ietf.nea.pt.value.enums.PtTlsMessageErrorCodeEnum;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.t.message.TransportMessage;
import de.hsbremen.tc.tnc.transport.exception.ConnectionException;

/**
 * Default version negotiation implementation for responder in
 * a the version negotiation phase of a transport connection.
 * This negotiator has side effects. A new negotiator must be
 * added for every connection that should be negotiated.
 * The negotiator is not reusable
 */
public class DefaultNegotiationResponder implements Negotiator {

    private static final NegotiatorTypeEnum TYPE =
            NegotiatorTypeEnum.NEG_RESPONDER;
    
    private final short minVersion;
    private final short maxVersion;
    private final short prefVersion;
    private short negotiatedVersion;

    /**
     * Creates a default responding version negotiator.
     * Version numbers must be >= 1.
     * 
     * @param minVersion the minimal supported version number
     * @param maxVersion the maximum supported version number
     * @param prefVersion the preferred version number
     */
    public DefaultNegotiationResponder(short minVersion, short maxVersion,
            short prefVersion) {
        
        if (minVersion < 1
                || maxVersion < minVersion
                || prefVersion < minVersion) {
            throw new IllegalArgumentException("Version information not valid. "
                    + "Minimal version must be > 0. "
                    + "Maximum version must be >= minimal version. "
                    + "prefered Version must be >= minimal version.");
        }
        
        this.minVersion = minVersion;
        this.maxVersion = maxVersion;
        this.prefVersion = prefVersion;
        this.negotiatedVersion = 0;
    }

    @Override
    public NegotiatorTypeEnum getType() {
        return TYPE;
    }
    
    @Override
    public short getNegotiatedVersion() {
        return this.negotiatedVersion;
    }

    @Override
    public void negotiate(SocketMessenger wrappedSocket)
            throws SerializationException, ValidationException,
            ConnectionException {

        TransportMessage message = null;

        while (message == null) {

            message = wrappedSocket.readFromStream();

            if (message != null) {
                if (message.getValue()
                        instanceof PtTlsMessageValueVersionRequest) {

                    this.chooseVersion(message);
                    
                    TransportMessage m = PtTlsMessageFactoryIetf
                            .createVersionResponse(
                                    wrappedSocket.getIdentifier(),
                                    this.negotiatedVersion);
                    wrappedSocket.writeToStream(m);
                    
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
    }

    /**
     * Handles the received version request message and sets
     * the {@link #negotiatedVersion} member variable.
     *
     * @param message the version request message
     * @throws ValidationException if the received version request does
     * not contain a version within the supported version range
     */
    private void chooseVersion(TransportMessage message)
            throws ValidationException {

        PtTlsMessageValueVersionRequest value =
                (PtTlsMessageValueVersionRequest) message.getValue();

        short versionNumber = 0;
        
        // if possible use prefered version from request
        if (minVersion <= value.getPreferredVersion()
                && maxVersion >= value.getPreferredVersion()) {
            versionNumber = value.getPreferredVersion();

            // or look for highest version
        } else {

            // test out of range
            if (this.maxVersion < value.getMinVersion()
                    || this.minVersion > value.getMaxVersion()) {
                versionNumber = 0;
                
            } else {

                versionNumber = (this.maxVersion > value.getMaxVersion())
                        ? value.getMaxVersion()
                    : ((this.maxVersion == value.getMaxVersion()) ? value
                            .getMaxVersion() : this.maxVersion);
            }
        }
        
        this.negotiatedVersion = versionNumber;
        
        if (this.negotiatedVersion <= 0) {

            throw new ValidationException(
                    "Version not supported.",
                    new RuleException(
                            "Version "
                              + prefVersion
                              + " not in supported version range.",
                            true,
                            PtTlsMessageErrorCodeEnum
                                .IETF_UNSUPPORTED_VERSION.code(),
                            PtTlsErrorCauseEnum
                                .TRANSPORT_VERSION_NOT_SUPPORTED
                                .id()),
                         16 + 1, message.getHeader());
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

        // An error violates the protocol flow.
        // In this regard even a minor error must
        // lead to a connection break up.
        throw new ConnectionException(
                "Error message received in negotiation phase: "
                        + error.toString() + "\n Connection will be closed.");

    }
}
