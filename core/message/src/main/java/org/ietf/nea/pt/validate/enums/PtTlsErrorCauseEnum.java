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
package org.ietf.nea.pt.validate.enums;

import de.hsbremen.tc.tnc.message.exception.RuleException;

/**
 * Enumeration of error causes according to invalid transport messages or
 * transport message values.
 *
 *
 */
public enum PtTlsErrorCauseEnum {
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
     * 3 - Length is to short.
     */
    LENGTH_TO_SHORT(3),
    /**
     * 4 - String is null terminated.
     */
    NULL_TERMINATION(4),
    /**
     * 5 - String has zero length.
     */
    ZERO_STRING(5),
    /**
     * 6 - Vendor ID not supported.
     */
    VENDOR_ID_NOT_SUPPORTED(6),
    /**
     * 7 - Message type not supported.
     */
    MESSAGE_TYPE_NOT_SUPPORTED(7),
    /**
     * 8 - Message type is unexpected.
     */
    MESSAGE_TYPE_UNEXPECTED(8),
    /**
     * 9 - Value to large.
     */
    VALUE_TO_LARGE(9),
    /**
     * 10 - Unsigned value cannot be negative.
     */
    NEGATIV_UNSIGNED(10),
    /**
     * 11 - SASL mechanism name could not be matched.
     */
    SASL_NAMING_MISMATCH(11),
    /**
     * 12 - SASL result is not supported.
     */
    SASL_RESULT_NOT_SUPPORTED(12),
    /**
     * 13 - Transport message version not supported.
     */
    TRANSPORT_VERSION_NOT_SUPPORTED(13),
    /**
     * 14 - Additional SASL authentication not supported.
     */
    ADDITIONAL_SASL_NOT_SUPPORTED(14);

    private int id;

    /**
     * Creates a cause with the given ID.
     *
     * @param id the cause ID
     */
    private PtTlsErrorCauseEnum(final int id) {
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
    public static PtTlsErrorCauseEnum fromId(final int id) {
        VENDOR_ID_RESERVED.id();
        switch (id) {
        case 1:
            return VENDOR_ID_RESERVED;
        case 2:
            return MESSAGE_TYPE_RESERVED;
        case 3:
            return LENGTH_TO_SHORT;
        case 4:
            return NULL_TERMINATION;
        case 5:
            return ZERO_STRING;
        case 6:
            return VENDOR_ID_NOT_SUPPORTED;
        case 7:
            return MESSAGE_TYPE_NOT_SUPPORTED;
        case 8:
            return MESSAGE_TYPE_UNEXPECTED;
        case 9:
            return VALUE_TO_LARGE;
        case 10:
            return NEGATIV_UNSIGNED;
        case 11:
            return SASL_NAMING_MISMATCH;
        case 12:
            return SASL_RESULT_NOT_SUPPORTED;
        case 13:
            return TRANSPORT_VERSION_NOT_SUPPORTED;
        case 14:
            return ADDITIONAL_SASL_NOT_SUPPORTED;
        default:
            return NOT_SPECIFIED;
        }
    }
}
