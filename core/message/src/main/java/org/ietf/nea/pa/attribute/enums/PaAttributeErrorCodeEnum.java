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
package org.ietf.nea.pa.attribute.enums;

/**
 * Enumeration of known integrity measurement attribute error codes.
 *
 *
 */
public enum PaAttributeErrorCodeEnum {
    /**
     * 0 - Reserved - Reserved code.
     */
    IETF_RESERVED(0),
    /**
     * 1 - Invalid Parameter - Error parameter has offset where invalid value
     * was found.
     */
    IETF_INVALID_PARAMETER(1),
    /**
     * 2 - Version Not Supported - Error parameter has information about which
     * versions are supported.
     */
    IETF_UNSUPPORTED_VERSION(2),
    /**
     * 3 - Unsupported Mandatory Attribute - Error parameter contains the header
     * of the offending attribute.
     */
    IETF_UNSUPPORTED_MANDATORY_ATTRIBUTE(3);

    private long code;

    /**
     * Creates an error code with the given code value.
     *
     * @param code the code value
     */
    private PaAttributeErrorCodeEnum(final long code) {
        this.code = code;
    }

    /**
     * Returns the value of the error code.
     *
     * @return the error code value
     */
    public long code() {
        return this.code;
    }

    /**
     * Returns an error code for the given code value.
     *
     * @param code the code value
     * @return an error code or null
     */
    public static PaAttributeErrorCodeEnum fromCode(final long code) {

        if (code == IETF_RESERVED.code) {
            return IETF_RESERVED;
        }

        if (code == IETF_INVALID_PARAMETER.code) {
            return IETF_INVALID_PARAMETER;
        }

        if (code == IETF_UNSUPPORTED_MANDATORY_ATTRIBUTE.code) {
            return IETF_UNSUPPORTED_MANDATORY_ATTRIBUTE;
        }

        if (code == IETF_UNSUPPORTED_VERSION.code) {
            return IETF_UNSUPPORTED_VERSION;
        }

        return null;
    }
}
