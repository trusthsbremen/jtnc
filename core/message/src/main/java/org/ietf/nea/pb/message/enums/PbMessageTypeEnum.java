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
