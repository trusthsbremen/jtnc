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
package de.hsbremen.tc.tnc.tnccs.session.base.simple;

import de.hsbremen.tc.tnc.HSBConstants;
import de.hsbremen.tc.tnc.attribute.TncAttributeType;
import de.hsbremen.tc.tnc.attribute.TncCommonAttributeTypeEnum;
import de.hsbremen.tc.tnc.attribute.TncHsbAttributeTypeEnum;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.exception.enums.TncExceptionCodeEnum;
import de.hsbremen.tc.tnc.message.TcgProtocolBindingIdentifier;
import de.hsbremen.tc.tnc.tnccs.session.base.SessionAttributes;

/**
 * Default attributes for a TNC(C/S) session.
 *
 *
 */
public class DefaultSessionAttributes implements SessionAttributes {

    private final TcgProtocolBindingIdentifier tnccsProtocol;
    private long currentRoundTrips;

    private String preferredLanguage;

    /**
     * Creates the default session attributes with a given protocol type
     * and version. The default preferred language is set to english
     * and the maximum message round trips are set to unknown.
     *
     * @param tnccsProtocol the IF-TNCCS protocol identifier
     */
    public DefaultSessionAttributes(
            final TcgProtocolBindingIdentifier tnccsProtocol) {
        this(tnccsProtocol, HSBConstants.HSB_DEFAULT_LANGUAGE);
    }

    /**
     * Creates the default session attributes with a given attribute values.
     *
     * @param tnccsProtocol the IF-TNCCS protocol identifier
     * @param preferredLanguage the preferred human readable language
     */
    public DefaultSessionAttributes(
            final TcgProtocolBindingIdentifier tnccsProtocol,
            final String preferredLanguage) {
        this.tnccsProtocol = tnccsProtocol;
        this.currentRoundTrips = 0;
        this.preferredLanguage = preferredLanguage;

    }

    @Override
    public TcgProtocolBindingIdentifier getTnccsProtocolIdentifier() {
        return this.tnccsProtocol;
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
            try {
                return this.tnccsProtocol.label();
            } catch (NullPointerException e) {
                throw new TncException("The attribute with ID " + type.id()
                        + " is unknown.",
                        TncExceptionCodeEnum.TNC_RESULT_INVALID_PARAMETER);
            }
        }

        if (type.equals(
                TncCommonAttributeTypeEnum.TNC_ATTRIBUTEID_IFTNCCS_VERSION)) {
            try {
                return this.tnccsProtocol.version();
            } catch (NullPointerException e) {
                throw new TncException("The attribute with ID " + type.id()
                        + " is unknown.",
                        TncExceptionCodeEnum.TNC_RESULT_INVALID_PARAMETER);
            }
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
        
        boolean attributeSet = false;
        if (type.equals(
                TncCommonAttributeTypeEnum
                .TNC_ATTRIBUTEID_PREFERRED_LANGUAGE)) {

            if (value instanceof String) {
                this.preferredLanguage = (String) value;
                attributeSet = true;
            }
        }

        if (!attributeSet) {
            throw new TncException("The attribute with ID " + type.id()
                    + " is unknown or not writeable.",
                    TncExceptionCodeEnum.TNC_RESULT_INVALID_PARAMETER);
        }
    }

}
