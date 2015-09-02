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
 * Enumeration of known integrity measurement attribute remediation parameter
 * types.
 *
 *
 */
public enum PaAttributeRemediationParameterTypeEnum {

    /**
     * 1 - URI - Remediation parameter containing a URI.
     */
    IETF_URI(1),
    /**
     * 2 - String - Remediation parameter containing a string.
     */
    IETF_STRING(2);

    private long id;

    /**
     * Creates the type with the given type ID.
     *
     * @param id the type ID
     */
    private PaAttributeRemediationParameterTypeEnum(final long id) {

        this.id = id;
    }

    /**
     * Returns the parameter type ID.
     *
     * @return the type ID
     */
    public long id() {
        return this.id;
    }

    /**
     * Returns the type for the given type ID.
     *
     * @param id the type ID
     * @return a type or null
     */
    public static PaAttributeRemediationParameterTypeEnum fromId(
            final long id) {
        if (id == IETF_URI.id) {
            return IETF_URI;
        }

        if (id == IETF_STRING.id) {
            return IETF_STRING;
        }

        return null;
    }

}
