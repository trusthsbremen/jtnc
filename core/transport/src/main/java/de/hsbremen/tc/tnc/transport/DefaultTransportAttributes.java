/**
 * The BSD 3-Clause License ("BSD New" or "BSD Simplified")
 *
 * Copyright (c) 2015, Carl-Heinz Genzel
 * All rights reserved.
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
package de.hsbremen.tc.tnc.transport;

import de.hsbremen.tc.tnc.HSBConstants;
import de.hsbremen.tc.tnc.attribute.TncAttributeType;
import de.hsbremen.tc.tnc.attribute.TncCommonAttributeTypeEnum;
import de.hsbremen.tc.tnc.attribute.TncHsbAttributeTypeEnum;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.exception.enums.TncExceptionCodeEnum;
import de.hsbremen.tc.tnc.message.TcgProtocolBindingIdentifier;

/**
 * Default attributes for a TransportConnection.
 *
 */
public class DefaultTransportAttributes implements TransportAttributes {

    private final String tId;
    private final TcgProtocolBindingIdentifier tProtocol;
    private final long maxMessageLength;
    private final long maxMessageLengthPerIm;
    private final long maxRoundTrips;

    /**
     * Creates a DefaultTransportAttributes object with a given ID and the
     * protocol type and version. Maximum message size and round trips are set
     * to unknown.
     *
     * @param tId the connection ID from a TransportConnection
     * @param tProtocolIdentifier the protocol identifier from
     * a TransportConnection
     */
    public DefaultTransportAttributes(final String tId,
            final TcgProtocolBindingIdentifier tProtocolIdentifier) {
        this(tId, tProtocolIdentifier,
                HSBConstants.TCG_IM_MAX_MESSAGE_SIZE_UNKNOWN,
                HSBConstants.HSB_TRSPT_MAX_MESSAGE_SIZE_UNKNOWN,
                HSBConstants.TCG_IM_MAX_ROUND_TRIPS_UNKNOWN);
    }

    /**
     * Creates a DefaultTransportAttributes object with a given ID and the
     * protocol type and version, as well as further important attributes.
     *
     * @param tId the connection ID from a TransportConnection
     * @param tProtocolIdentifier the protocol identifier from
     * a TransportConnection
     * @param maxMessageLength the maximum full message length
     * @param maxMessageLengthPerIm the maximum message length of an IF-M
     * message
     * @param maxRoundTrips the maximum round trips
     */
    public DefaultTransportAttributes(final String tId,
            final TcgProtocolBindingIdentifier tProtocolIdentifier,
            final long maxMessageLength,
            final long maxMessageLengthPerIm, final long maxRoundTrips) {
        this.tId = tId;
        this.tProtocol = tProtocolIdentifier;
        this.maxMessageLength = maxMessageLength;
        this.maxMessageLengthPerIm = maxMessageLengthPerIm;
        this.maxRoundTrips = maxRoundTrips;
    }

    @Override
    public String getTransportId() {
        return this.tId;
    }

    @Override
    public TcgProtocolBindingIdentifier getTransportProtocolIdentifier() {
        return this.tProtocol;
    }


    @Override
    public long getMaxMessageLength() {
        return this.maxMessageLength;
    }

    @Override
    public long getMaxMessageLengthPerIm() {
        return this.maxMessageLengthPerIm;
    }

    @Override
    public long getMaxRoundTrips() {
        return this.maxRoundTrips;
    }

    @Override
    public Object getAttribute(final TncAttributeType type)
            throws TncException {

        if (type.equals(TncHsbAttributeTypeEnum.HSB_ATTRIBUTEID_IFT_ID)) {
            return this.tId;
        }

        if (type.equals(
                TncCommonAttributeTypeEnum.TNC_ATTRIBUTEID_IFT_PROTOCOL)) {
            try {
                return this.tProtocol.label();
            } catch (NullPointerException e) {
                throw new TncException("The attribute with ID " + type.id()
                        + " is unknown.",
                        TncExceptionCodeEnum.TNC_RESULT_INVALID_PARAMETER);
            }
        }

        if (type.equals(
                TncCommonAttributeTypeEnum.TNC_ATTRIBUTEID_IFT_VERSION)) {
            try {
                return this.tProtocol.version();
            } catch (NullPointerException e) {
                throw new TncException("The attribute with ID " + type.id()
                        + " is unknown.",
                        TncExceptionCodeEnum.TNC_RESULT_INVALID_PARAMETER);
            }
        }

        if (type.equals(
                TncCommonAttributeTypeEnum.TNC_ATTRIBUTEID_MAX_MESSAGE_SIZE)) {
            return this.maxMessageLengthPerIm;
        }

        if (type.equals(
                TncCommonAttributeTypeEnum.TNC_ATTRIBUTEID_MAX_ROUND_TRIPS)) {
            return this.maxRoundTrips;
        }

        throw new TncException("The attribute with ID " + type.id()
                + " is unknown.",
                TncExceptionCodeEnum.TNC_RESULT_INVALID_PARAMETER);
    }

    @Override
    public void setAttribute(final TncAttributeType type, final Object value)
            throws TncException {
        throw new UnsupportedOperationException(
                "The operation setAttribute(...) "
                        + "is not supported, because there "
                        + "are no attributes to set.");
    }

}
