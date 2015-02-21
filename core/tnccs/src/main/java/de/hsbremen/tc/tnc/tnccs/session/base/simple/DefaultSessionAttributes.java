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
package de.hsbremen.tc.tnc.tnccs.session.base.simple;

import de.hsbremen.tc.tnc.HSBConstants;
import de.hsbremen.tc.tnc.attribute.TncAttributeType;
import de.hsbremen.tc.tnc.attribute.TncCommonAttributeTypeEnum;
import de.hsbremen.tc.tnc.attribute.TncHsbAttributeTypeEnum;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.exception.enums.TncExceptionCodeEnum;
import de.hsbremen.tc.tnc.tnccs.session.base.SessionAttributes;

/**
 * Default attributes for a TNC(C/S) session.
 *
 * @author Carl-Heinz Genzel
 *
 */
public class DefaultSessionAttributes implements SessionAttributes {

    private final String tnccsProtocol;
    private final String tnccsVersion;
    private long currentRoundTrips;

    private String preferredLanguage;

    /**
     * Creates the default session attributes with a given protocol type
     * and version. The default preferred language is set to english
     * and the maximum message round trips are set to unknown.
     *
     * @param tnccsProtocol the IF-TNCCS protocol type
     * @param tnccsVersion the IF-TNCCS protocol version
     */
    public DefaultSessionAttributes(final String tnccsProtocol,
            final String tnccsVersion) {
        this(tnccsProtocol, tnccsVersion, HSBConstants.HSB_DEFAULT_LANGUAGE,
                HSBConstants.TCG_IM_MAX_ROUND_TRIPS_UNKNOWN);
    }

    /**
     * Creates the default session attributes with a given attribute values.
     *
     * @param tnccsProtocol the IF-TNCCS protocol type
     * @param tnccsVersion the IF-TNCCS protocol version
     * @param preferredLanguage the preferred human readable language
     * @param maxRoundTrips the maximum message round trips
     */
    public DefaultSessionAttributes(final String tnccsProtocol,
            final String tnccsVersion, final String preferredLanguage,
            final long maxRoundTrips) {
        this.tnccsProtocol = tnccsProtocol;
        this.tnccsVersion = tnccsVersion;
        this.currentRoundTrips = 0;
        this.preferredLanguage = preferredLanguage;

    }

    @Override
    public String getTnccsProtocolType() {
        return this.tnccsProtocol;
    }

    @Override
    public String getTnccsProtocolVersion() {
        return this.tnccsVersion;
    }

    @Override
    public long getCurrentRoundTrips() {
        return this.currentRoundTrips;
    }

    @Override
    public String getPreferredLanguage() {
        return this.preferredLanguage;
    }

    @Override
    public void setCurrentRoundTrips(final long roundtrips) {
        this.currentRoundTrips = (roundtrips >= 0) ? roundtrips : 0;
    }

    @Override
    public void setPreferredLanguage(final String preferredLanguage) {
        if (preferredLanguage != null && !preferredLanguage.isEmpty()) {
            this.preferredLanguage = preferredLanguage;
        }
    }

    @Override
    public Object getAttribute(final TncAttributeType type)
            throws TncException {
        if (type.equals(
                TncCommonAttributeTypeEnum.TNC_ATTRIBUTEID_IFTNCCS_PROTOCOL)) {
            return this.tnccsProtocol;
        }

        if (type.equals(
                TncCommonAttributeTypeEnum.TNC_ATTRIBUTEID_IFTNCCS_VERSION)) {
            return this.tnccsVersion;
        }

        if (type.equals(
                TncCommonAttributeTypeEnum
                .TNC_ATTRIBUTEID_PREFERRED_LANGUAGE)) {
            return this.preferredLanguage;
        }

        if (type.equals(
                TncHsbAttributeTypeEnum.HSB_ATTRIBUTEID_CURRENT_ROUND_TRIPS)) {
            return this.currentRoundTrips;
        }

        throw new TncException("The attribute with ID " + type.id()
                + " is unknown.",
                TncExceptionCodeEnum.TNC_RESULT_INVALID_PARAMETER);
    }

    @Override
    public void setAttribute(final TncAttributeType type, final Object value)
            throws TncException {
        if (type.equals(
                TncCommonAttributeTypeEnum
                .TNC_ATTRIBUTEID_PREFERRED_LANGUAGE)) {

            if (value instanceof String) {
                this.preferredLanguage = (String) value;
            }
        }

        throw new TncException("The attribute with ID " + type.id()
                + " is unknown or not writeable.",
                TncExceptionCodeEnum.TNC_RESULT_INVALID_PARAMETER);
    }

}
