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
package org.ietf.nea.pa.validate.enums;

import de.hsbremen.tc.tnc.message.exception.RuleException;

/**
 * Enumeration of error causes according to invalid integrity measurement
 * messages, attributes or integrity measurement attribute values.
 *
 *
 */
public enum PaErrorCauseEnum {

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
    TYPE_RESERVED(2),
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
     * 6 - Attribute request illegal.
     */
    ILLEGAL_ATTRIBUTE_REQUEST(6),
    /**
     * 7 - Message version not supported.
     */
    MESSAGE_VERSION_NOT_SUPPORTED(7),
    /**
     * 8 - Vendor ID not supported.
     */
    VENDOR_ID_NOT_SUPPORTED(8),
    /**
     * 9 - Message type not supported.
     */
    MESSAGE_TYPE_NOT_SUPPORTED(9),
    /**
     * 10 - URI syntax not valid.
     */
    URI_SYNTAX_NOT_VALID(10),
    /**
     * 11 - Value to large.
     */
    VALUE_TO_LARGE(11),
    /**
     * 12 - Unsigned value cannot be negative.
     */
    NEGATIV_UNSIGNED(12),
    /**
     * 13 - Assessment Result not supported.
     */
    ASSESSMENT_RESULT_NOT_SUPPORTED(13),
    /**
     * 14 - Traffic forwarding status not supported.
     */
    FORWARDING_STATUS_NOT_SUPPORTED(14),
    /**
     * 15 - Factory default password status not supported.
     */
    FACTORY_DEFAULT_PW_STATUS_NOT_SUPPORTED(15),
    /**
     * 16 - Invalid product ID.
     */
    INVALID_PRODUCT_ID(16),
    /**
     * 17 - Duplicate port filter entry.
     */
    DUPLICATE_PORT_ENTRY(17),
    /**
     * 18 - Mixed blocking status for one protocol.
     */
    MIXED_PROTOCOL_BLOCKING(18),
    /**
     * 19 - Time format not valid.
     */
    TIME_FORMAT_NOT_VALID(19),
    /**
     * 20 - No skip missing.
     */
    NOSKIP_MISSING(20),
    /**
     * 21 - No skip not allowed.
     */
    NOSKIP_NOT_ALLOWED(21);

    private int id;

    /**
     * Creates a cause with the given ID.
     * @param id the cause ID
     */
    private PaErrorCauseEnum(final int id) {
        this.id = id;
    }

    /**
     * Returns the cause ID.
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
    public static PaErrorCauseEnum fromId(final int id) {
        VENDOR_ID_RESERVED.id();
        switch (id) {
        case 1:
            return VENDOR_ID_RESERVED;
        case 2:
            return TYPE_RESERVED;
        case 3:
            return LENGTH_TO_SHORT;
        case 4:
            return NULL_TERMINATION;
        case 5:
            return ZERO_STRING;
        case 6:
            return ILLEGAL_ATTRIBUTE_REQUEST;
        case 7:
            return MESSAGE_VERSION_NOT_SUPPORTED;
        case 8:
            return VENDOR_ID_NOT_SUPPORTED;
        case 9:
            return MESSAGE_TYPE_NOT_SUPPORTED;
        case 10:
            return URI_SYNTAX_NOT_VALID;
        case 11:
            return VALUE_TO_LARGE;
        case 12:
            return NEGATIV_UNSIGNED;
        case 13:
            return ASSESSMENT_RESULT_NOT_SUPPORTED;
        case 14:
            return FORWARDING_STATUS_NOT_SUPPORTED;
        case 15:
            return FACTORY_DEFAULT_PW_STATUS_NOT_SUPPORTED;
        case 16:
            return INVALID_PRODUCT_ID;
        case 17:
            return DUPLICATE_PORT_ENTRY;
        case 18:
            return MIXED_PROTOCOL_BLOCKING;
        case 19:
            return TIME_FORMAT_NOT_VALID;
        case 20:
            return NOSKIP_MISSING;
        case 21:
            return NOSKIP_NOT_ALLOWED;
        default:
            return NOT_SPECIFIED;
        }
    }
}
