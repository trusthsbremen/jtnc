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
package org.ietf.nea.pb.message.enums;


/**
 * Enumeration of known TNCCS message types.
 *
 *
 */
public enum PbMessageTypeEnum {

    /**
     * 0 - PB-Experimental - reserved for experimental use.
     */
    IETF_PB_EXPERIMENTAL(0),
    /**
     * 1 - PB-PA - contains a PA message.
     */
    IETF_PB_PA(1),
    /**
     * 2 - PB-Assessment-Result - the overall assessment result computed by the
     * Posture Broker Server.
     */
    IETF_PB_ASSESSMENT_RESULT(2),
    /**
     * 3 - PB-Access-Recommendation - includes Posture Broker Server access
     * recommendation.
     */
    IETF_PB_ACCESS_RECOMMENDATION(3),
    /**
     * 4 - PB-Remediation-Parameters - includes Posture Broker Server
     * remediation parameters.
     */
    IETF_PB_REMEDIATION_PARAMETERS(4),
    /**
     * 5 - PB-Error - error indicator.
     */
    IETF_PB_ERROR(5),
    /**
     * 6 - PB-Language-Preference - sender's preferred language(s) for
     * human-readable strings.
     */
    IETF_PB_LANGUAGE_PREFERENCE(6),
    /**
     * 7 - PB-Reason-String - string explaining reason for Posture Broker Server
     * access recommendation.
     */
    IETF_PB_REASON_STRING(7),
    /**
     * Reserved - for message routing.
     */
    IETF_PB_RESERVED(0xffffffffL);

    private long id;

    /**
     * Creates the type with the given type ID.
     *
     * @param id the type ID
     */
    private PbMessageTypeEnum(final long id) {
        this.id = id;
    }

    /**
     * Returns the message type ID.
     *
     * @return the type ID
     */
    public long id() {
        return this.id;
    }

    /**
     * Returns the type for the given type ID.
     *
     * @param id the type ID
     * @return a type or null
     */
    public static PbMessageTypeEnum fromId(final long id) {

        if (id == IETF_PB_EXPERIMENTAL.id) {
            return IETF_PB_EXPERIMENTAL;
        }

        if (id == IETF_PB_PA.id) {
            return IETF_PB_PA;
        }

        if (id == IETF_PB_ASSESSMENT_RESULT.id) {
            return IETF_PB_ASSESSMENT_RESULT;
        }

        if (id == IETF_PB_ACCESS_RECOMMENDATION.id) {
            return IETF_PB_ACCESS_RECOMMENDATION;
        }

        if (id == IETF_PB_REMEDIATION_PARAMETERS.id) {
            return IETF_PB_REMEDIATION_PARAMETERS;
        }

        if (id == IETF_PB_ERROR.id) {
            return IETF_PB_ERROR;
        }

        if (id == IETF_PB_LANGUAGE_PREFERENCE.id) {
            return IETF_PB_LANGUAGE_PREFERENCE;
        }

        if (id == IETF_PB_REASON_STRING.id) {
            return IETF_PB_REASON_STRING;
        }

        if (id == IETF_PB_RESERVED.id) {
            return IETF_PB_RESERVED;
        }

        return null;
    }
}
