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
