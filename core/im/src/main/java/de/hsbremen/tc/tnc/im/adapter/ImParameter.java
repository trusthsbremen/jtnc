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
package de.hsbremen.tc.tnc.im.adapter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import de.hsbremen.tc.tnc.HSBConstants;
import de.hsbremen.tc.tnc.report.SupportedMessageType;

/**
 * Generic IM(C/V) parameter set.
 *
 *
 */
public class ImParameter {

    private Set<SupportedMessageType> supportedMessageTypes;
    private boolean tncsFirstSupport;
    private long primaryId;
    private String preferredLanguage;

    /**
     * Creates a parameter set with default values.
     *
     * <ul>
     * <li>primary ID = -1 (ID unknown)</li>
     * <li>TNCS first support = false</li>
     * <li>preferred language = en (English)</li>
     * </ul>
     */
    public ImParameter() {
        this(HSBConstants.HSB_IM_ID_UNKNOWN, false, "Accept-Language: en");
    }

    /**
     * Creates a parameter set with the TNCS first support specifically set. All
     * other parameters have default values.
     *
     * @param tncsFirstSupport true if TNCS fist is supported
     */
    public ImParameter(final boolean tncsFirstSupport) {
        this(HSBConstants.HSB_IM_ID_UNKNOWN,
                tncsFirstSupport, "Accept-Language: en");
    }

    /**
     * Creates a custom parameter set. The preferred language is set
     * as Accept-Language header (RFC 3282) (e.g. Accept-Language: en).
     *
     * @param primaryId the primary ID
     * @param tncsFirstSupport true if TNCS fist is supported
     * @param preferredLanguage the preferred language
     */
    public ImParameter(final long primaryId, final boolean tncsFirstSupport,
            final String preferredLanguage) {
        this.setPrimaryId(primaryId);
        this.setTncsFirstSupport(tncsFirstSupport);
        this.setPreferredLanguage(preferredLanguage);
        this.supportedMessageTypes = new HashSet<>();

    }

    /**
     * Returns if TNCS first is supported.
     * @return true if TNCS first is supported
     */
    public boolean hasTncsFirstSupport() {
        return this.tncsFirstSupport;
    }

    /**
     * Sets if TNCS first is supported.
     * @param supportsTncsFirst true if TNCS first is supported
     */
    public void setTncsFirstSupport(final boolean supportsTncsFirst) {
        this.tncsFirstSupport = supportsTncsFirst;
    }

    /**
     * Returns the primary ID.
     * @return the primary ID
     */
    public long getPrimaryId() {
        return primaryId;
    }

    /**
     * Sets the primary ID.
     * @param primaryId the primary ID
     */
    public void setPrimaryId(final long primaryId) {
        this.primaryId = primaryId;
    }

    /**
     * Returns the supported component message types.
     * @return the list of supported component message types
     */
    public Set<SupportedMessageType> getSupportedMessageTypes() {
        return Collections.unmodifiableSet(supportedMessageTypes);
    }

    /**
     * Sets the supported component message types.
     * @param supportedMessageTypes the list of supported
     * component message types
     */
    public void setSupportedMessageTypes(
            final Set<SupportedMessageType> supportedMessageTypes) {
        this.supportedMessageTypes = supportedMessageTypes;
    }

    /**
     * Returns the preferred language.
     * @return the preferred language
     */
    public String getPreferredLanguage() {
        return this.preferredLanguage;
    }

    /**
     * Sets the preferred language.
     * @param preferredLanguage the preferred language
     * as Accept-Language header (RFC 3282)
     */
    public void setPreferredLanguage(final String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }


}
