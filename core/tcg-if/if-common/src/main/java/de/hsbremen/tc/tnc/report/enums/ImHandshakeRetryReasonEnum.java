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
package de.hsbremen.tc.tnc.report.enums;

import org.trustedcomputinggroup.tnc.ifimc.IMCConnection;
import org.trustedcomputinggroup.tnc.ifimv.IMVConnection;

/**
 * Enumeration of known retry reasons from client and server.
 *
 *
 */
public enum ImHandshakeRetryReasonEnum {

    /* IMC */
    /**
     * Client retry reason remediation complete. May only be used for retry on a
     * single connection.
     */
    TNC_RETRY_REASON_IMC_REMEDIATION_COMPLETE(
            IMCConnection.TNC_RETRY_REASON_IMC_REMEDIATION_COMPLETE),

    /**
     * Client retry reason serious event. May be used for global or
     * single retry on all/one connection(s).
     */
    TNC_RETRY_REASON_IMC_SERIOUS_EVENT(
            IMCConnection.TNC_RETRY_REASON_IMC_SERIOUS_EVENT),

    /**
     * Client retry reason informational event. May be used for global or
     * single retry on all/one connection(s).
     */
    TNC_RETRY_REASON_IMC_INFORMATIONAL_EVENT(
            IMCConnection.TNC_RETRY_REASON_IMC_INFORMATIONAL_EVENT),

    /**
     * Client retry reason periodic retry. May be used for global or
     * single retry on all/one connection(s).
     */
    TNC_RETRY_REASON_IMC_PERIODIC(IMCConnection.TNC_RETRY_REASON_IMC_PERIODIC),

    /* IMV */
    /**
     * Server retry reason important policy change. May be used for global or
     * single retry on all/one connection(s).
     */
    TNC_RETRY_REASON_IMV_IMPORTANT_POLICY_CHANGE(
            IMVConnection.TNC_RETRY_REASON_IMV_IMPORTANT_POLICY_CHANGE),

    /**
     * Server retry reason minor policy change. May be used for global or
     * single retry on all/one connection(s).
     */
    TNC_RETRY_REASON_IMV_MINOR_POLICY_CHANGE(
            IMVConnection.TNC_RETRY_REASON_IMV_MINOR_POLICY_CHANGE),

    /**
     * Server retry reason serious event. May be used for global or
     * single retry on all/one connection(s).
     */
    TNC_RETRY_REASON_IMV_SERIOUS_EVENT(
            IMVConnection.TNC_RETRY_REASON_IMV_SERIOUS_EVENT),

    /**
     * Server retry reason minor event. May be used for global or
     * single retry on all/one connection(s).
     */
    TNC_RETRY_REASON_IMV_MINOR_EVENT(
            IMVConnection.TNC_RETRY_REASON_IMV_MINOR_EVENT),

    /**
     * Server retry reason periodic retry. May be used for global or
     * single retry on all/one connection(s).
     */
    TNC_RETRY_REASON_IMV_PERIODIC(IMVConnection.TNC_RETRY_REASON_IMV_PERIODIC);

    private final long id;

    /**
     * Creates a retry reason with an ID.
     * @param id the reason ID
     */
    private ImHandshakeRetryReasonEnum(final long id) {
        this.id = id;
    }

    /**
     * Returns the reason ID.
     * @return the reason ID
     */
    public long id() {
        return this.id;
    }

    /**
     * Returns a retry reason for the given ID.
     *
     * @param id the reason ID
     * @return the retry reason or null
     */
    public static ImHandshakeRetryReasonEnum fromId(final long id) {
        /* IMC */
        if (id == TNC_RETRY_REASON_IMC_REMEDIATION_COMPLETE.id) {
            return TNC_RETRY_REASON_IMC_REMEDIATION_COMPLETE;
        }

        if (id == TNC_RETRY_REASON_IMC_SERIOUS_EVENT.id) {
            return TNC_RETRY_REASON_IMC_SERIOUS_EVENT;
        }

        if (id == TNC_RETRY_REASON_IMC_INFORMATIONAL_EVENT.id) {
            return TNC_RETRY_REASON_IMC_INFORMATIONAL_EVENT;
        }

        if (id == TNC_RETRY_REASON_IMC_PERIODIC.id) {
            return TNC_RETRY_REASON_IMC_PERIODIC;
        }

        /* IMV */
        if (id == TNC_RETRY_REASON_IMV_IMPORTANT_POLICY_CHANGE.id) {
            return TNC_RETRY_REASON_IMV_IMPORTANT_POLICY_CHANGE;
        }

        if (id == TNC_RETRY_REASON_IMV_MINOR_POLICY_CHANGE.id) {
            return TNC_RETRY_REASON_IMV_MINOR_POLICY_CHANGE;
        }

        if (id == TNC_RETRY_REASON_IMV_SERIOUS_EVENT.id) {
            return TNC_RETRY_REASON_IMV_SERIOUS_EVENT;
        }

        if (id == TNC_RETRY_REASON_IMV_MINOR_EVENT.id) {
            return TNC_RETRY_REASON_IMV_MINOR_EVENT;
        }

        if (id == TNC_RETRY_REASON_IMV_PERIODIC.id) {
            return TNC_RETRY_REASON_IMV_PERIODIC;
        }

        return null;
    }
}
