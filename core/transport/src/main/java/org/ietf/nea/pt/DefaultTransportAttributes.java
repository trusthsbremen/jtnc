/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Carl-Heinz Genzel
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 */
package org.ietf.nea.pt;

import de.hsbremen.tc.tnc.HSBConstants;
import de.hsbremen.tc.tnc.attribute.TncAttributeType;
import de.hsbremen.tc.tnc.attribute.TncCommonAttributeTypeEnum;
import de.hsbremen.tc.tnc.attribute.TncHsbAttributeTypeEnum;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.exception.enums.TncExceptionCodeEnum;
import de.hsbremen.tc.tnc.transport.TransportAttributes;

/**
 * Default attributes for a TransportConnection.
 *
 * @author Carl-Heinz Genzel
 *
 */
public class DefaultTransportAttributes implements TransportAttributes {

    private final String tId;
    private final String tVersion;
    private final String tProtocol;
    private final long maxMessageLength;
    private final long maxMessageLengthPerIm;
    private final long maxRoundTrips;

    /**
     * Creates a DefaultTransportAttributes object with a given ID and the
     * protocol type and version. Maximum message size and round trips are set
     * to unknown.
     *
     * @param tId the connection ID from a TransportConnection
     * @param tProtocol the protocol type from a TransportConnection
     * @param tVersion the protocol version from a TransportConnection
     */
    public DefaultTransportAttributes(final String tId, final String tProtocol,
            final String tVersion) {
        this(tId, tProtocol, tVersion,
                HSBConstants.TCG_IM_MAX_MESSAGE_SIZE_UNKNOWN,
                HSBConstants.HSB_TRSPT_MAX_MESSAGE_SIZE_UNKNOWN,
                HSBConstants.TCG_IM_MAX_ROUND_TRIPS_UNKNOWN);
    }

    /**
     * Creates a DefaultTransportAttributes object with a given ID and the
     * protocol type and version, as well as further important attributes.
     *
     * @param tId the connection ID from a TransportConnection
     * @param tProtocol the protocol type from a TransportConnection
     * @param tVersion the protocol version from a TransportConnection
     * @param maxMessageLength the maximum full message length
     * @param maxMessageLengthPerIm the maximum message length of an IF-M
     * message
     * @param maxRoundTrips the maximum round trips
     */
    public DefaultTransportAttributes(final String tId, final String tProtocol,
            final String tVersion, final long maxMessageLength,
            final long maxMessageLengthPerIm, final long maxRoundTrips) {
        this.tId = tId;
        this.tProtocol = tProtocol;
        this.tVersion = tVersion;
        this.maxMessageLength = maxMessageLength;
        this.maxMessageLengthPerIm = maxMessageLengthPerIm;
        this.maxRoundTrips = maxRoundTrips;
    }

    @Override
    public String getTransportId() {
        return this.tId;
    }

    @Override
    public String getTransportVersion() {
        return this.tVersion;
    }

    @Override
    public String getTransportProtocol() {
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
            return this.tProtocol;
        }

        if (type.equals(
                TncCommonAttributeTypeEnum.TNC_ATTRIBUTEID_IFT_VERSION)) {
            return this.tVersion;
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
