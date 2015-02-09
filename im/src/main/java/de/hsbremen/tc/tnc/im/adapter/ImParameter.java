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
package de.hsbremen.tc.tnc.im.adapter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import de.hsbremen.tc.tnc.HSBConstants;
import de.hsbremen.tc.tnc.report.SupportedMessageType;

/**
 * Generic IM(C/V) parameter set.
 *
 * @author Carl-Heinz Genzel
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
