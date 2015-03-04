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
package de.hsbremen.tc.tnc.report.enums;

import org.trustedcomputinggroup.tnc.ifimc.IMCConnection;
import org.trustedcomputinggroup.tnc.ifimv.IMVConnection;

/**
 * Enumeration of known retry reasons from client and server.
 *
 * @author Carl-Heinz Genzel
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
