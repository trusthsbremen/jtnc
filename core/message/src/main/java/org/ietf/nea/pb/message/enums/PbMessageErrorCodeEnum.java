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
 * Enumeration of known TNCCS message error codes.
 *
 */
public enum PbMessageErrorCodeEnum {
    /**
     * 0 - Unexpected Batch Type - Error Parameters are empty.
     *
     */
    IETF_UNEXPECTED_BATCH_TYPE(0),
    /**
     * 1 - Invalid Parameter - Error Parameters has offset where invalid value
     * was found.
     *
     */
    IETF_INVALID_PARAMETER(1),
    /**
     * 2 - Local Error - Error Parameters are empty.
     *
     */
    IETF_LOCAL(2),
    /**
     * 3 - Unsupported Mandatory Message - Error Parameters has offset of
     * offending PB-TNC Message.
     */
    IETF_UNSUPPORTED_MANDATORY_MESSAGE(3),
    /**
     * 4 - Version Not Supported - Error Parameters has information about which
     * versions are supported.
     */
    IETF_UNSUPPORTED_VERSION(4);

    private int code;

    /**
     * Creates an error code with the given code
     * value.
     * @param code the code value
     */
    private PbMessageErrorCodeEnum(final int code) {
        this.code = code;
    }

    /**
     * Returns the value of the error code.
     * @return the error code value
     */
    public int code() {
        return this.code;
    }

    /**
     * Returns an error code for the given code value.
     * @param code the code value
     * @return an error code or null
     */
    public static PbMessageErrorCodeEnum fromCode(final int code) {

        if (code == IETF_UNEXPECTED_BATCH_TYPE.code) {
            return IETF_UNEXPECTED_BATCH_TYPE;
        }

        if (code == IETF_INVALID_PARAMETER.code) {
            return IETF_INVALID_PARAMETER;
        }

        if (code == IETF_LOCAL.code) {
            return IETF_LOCAL;
        }

        if (code == IETF_UNSUPPORTED_MANDATORY_MESSAGE.code) {
            return IETF_UNSUPPORTED_MANDATORY_MESSAGE;
        }

        if (code == IETF_UNSUPPORTED_VERSION.code) {
            return IETF_UNSUPPORTED_VERSION;
        }

        return null;
    }
}
