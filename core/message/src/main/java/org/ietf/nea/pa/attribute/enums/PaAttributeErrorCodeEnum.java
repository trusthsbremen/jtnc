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
