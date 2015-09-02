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
package org.ietf.nea.pb.validate.enums;

import de.hsbremen.tc.tnc.message.exception.RuleException;

/**
 * Enumeration of error causes according to invalid TNCCS messages or TNCCS
 * message values.
 *
 *
 */
public enum PbErrorCauseEnum {

    /**
     * 0 - Cause not specified.
     */
    NOT_SPECIFIED(RuleException.NOT_SPECIFIED),
    /**
     * 1 - Vendor ID is reserved.
     */
    VENDOR_ID_RESERVED(1),
    /**
     * 2 - Type ID is reserved.
     */
    MESSAGE_TYPE_RESERVED(2),
    /**
     * 3 - Integrity measurement component vendor ID is reserved.
     */
    SUB_VENDOR_ID_RESERVED(3),
    /**
     * 4 - Integrity measurement component type ID is reserved.
     */
    SUB_TYPE_RESERVED(4),
    /**
     * 5 - No skip not allowed.
     */
    NOSKIP_NOT_ALLOWED(5),
    /**
     * 6 - No skip missing.
     */
    NOSKIP_MISSING(6),
    /**
     * 7 - Length is to short.
     */
    LENGTH_TO_SHORT(7),
    /**
     * 8 - String is null terminated.
     */
    NULL_TERMINATION(8),
    /**
     * 9 - String has zero length.
     */
    ZERO_STRING(9),
    /**
     * 10 - Vendor ID not supported.
     */
    VENDOR_ID_NOT_SUPPORTED(10),
    /**
     * 11 - Message type not supported.
     */
    MESSAGE_TYPE_NOT_SUPPORTED(11),
    /**
     * 12 -Batch direction does not fit to the expected direction.
     */
    BATCH_DIRECTION_OR_TYPE_UNEXPECTED(12),
    /**
     * 13 - Batch version not supported.
     */
    BATCH_VERSION_NOT_SUPPORTED(13),
    /**
     * 14 - Exclusive delivery not possible.
     */
    EXCL_DELIVERY_NOT_POSSIBLE(14),
    /**
     * 15 - URI syntax not valid.
     */
    URI_SYNTAX_NOT_VALID(15),
    /**
     * 16 - Batch of type result does not contain an assessment result.
     */
    BATCH_RESULT_NO_ASSESSMENT_RESULT(16),
    /**
     * 17 - Value to large.
     */
    VALUE_TO_LARGE(17),
    /**
     * 18 - Unsigned value cannot be negative.
     */
    NEGATIV_UNSIGNED(18),
    /**
     * 19 - Assessment result not supported.
     */
    ASSESSMENT_RESULT_NOT_SUPPORTED(19),
    /**
     * 20 - Access recommendation not supported.
     */
    ACCESS_RECOMMENDATION_NOT_SUPPORTED(20);

    private int id;

    /**
     * Creates a cause with the given ID.
     *
     * @param id the cause ID
     */
    private PbErrorCauseEnum(final int id) {
        this.id = id;
    }

    /**
     * Returns the cause ID.
     *
     * @return the cause ID
     */
    public int id() {
        return this.id;
    }

    /**
     * Returns the cause for the given cause ID.
     *
     * @param id the cause ID
     * @return a cause
     */
    public static PbErrorCauseEnum fromId(final int id) {
        VENDOR_ID_RESERVED.id();
        switch (id) {
        case 1:
            return VENDOR_ID_RESERVED;
        case 2:
            return MESSAGE_TYPE_RESERVED;
        case 3:
            return SUB_VENDOR_ID_RESERVED;
        case 4:
            return SUB_TYPE_RESERVED;
        case 5:
            return NOSKIP_NOT_ALLOWED;
        case 6:
            return NOSKIP_MISSING;
        case 7:
            return LENGTH_TO_SHORT;
        case 8:
            return NULL_TERMINATION;
        case 9:
            return ZERO_STRING;
        case 10:
            return VENDOR_ID_NOT_SUPPORTED;
        case 11:
            return MESSAGE_TYPE_NOT_SUPPORTED;
        case 12:
            return BATCH_DIRECTION_OR_TYPE_UNEXPECTED;
        case 13:
            return BATCH_VERSION_NOT_SUPPORTED;
        case 14:
            return EXCL_DELIVERY_NOT_POSSIBLE;
        case 15:
            return URI_SYNTAX_NOT_VALID;
        case 16:
            return BATCH_RESULT_NO_ASSESSMENT_RESULT;
        case 17:
            return VALUE_TO_LARGE;
        case 18:
            return NEGATIV_UNSIGNED;
        case 19:
            return ASSESSMENT_RESULT_NOT_SUPPORTED;
        case 20:
            return ACCESS_RECOMMENDATION_NOT_SUPPORTED;
        default:
            return NOT_SPECIFIED;
        }
    }
}
