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
 * Enumeration of known integrity measurement attribute remediation parameter
 * types.
 *
 * @author Carl-Heinz Genzel
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
