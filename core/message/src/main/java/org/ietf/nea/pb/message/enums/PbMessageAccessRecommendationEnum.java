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
 * Enumeration of known TNCCS access recommendations.
 *
 *
 */
public enum PbMessageAccessRecommendationEnum {

    /**
     * 1 - Access Allowed - Full access is recommended.
     */
    ALLOWED(1),
    /**
     * 2 - Access Denied - No access is recommended.
     */
    DENIED(2),
    /**
     * 3 - Quarantined - Partial access is recommended to enable certain
     * activities such as updates.
     */
    QURANTINED(3);

    private int id;

    /**
     * Creates the access recommendation with the given result ID.
     * @param id the result ID
     */
    private PbMessageAccessRecommendationEnum(final int id) {
        this.id = id;
    }

    /**
     * Returns the access recommendation ID.
     *
     * @return the recommendation ID
     */
    public int id() {
        return this.id;
    }

    /**
     * Returns the access recommendation for the given
     * recommendation ID.
     *
     * @param id the recommendation ID
     * @return a recommendation or null
     */
    public static PbMessageAccessRecommendationEnum fromId(final int id) {

        if (id == ALLOWED.id) {
            return ALLOWED;
        }

        if (id == QURANTINED.id) {
            return QURANTINED;
        }

        if (id == DENIED.id) {
            return DENIED;
        }

        return null;
    }
}
