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
package org.ietf.nea.pt.validate.enums;

import de.hsbremen.tc.tnc.message.exception.RuleException;

/**
 * Enumeration of error causes according to invalid transport messages or
 * transport message values.
 *
 * @author Carl-Heinz Genzel
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
