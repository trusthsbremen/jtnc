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
 * Enumeration of known integrity measurement values for the result of an
 * operation's last use.
 *
 *
 */
public enum PaAttributeOperationLastResultEnum {

    /**
     * 0 - Unknown or other result.
     */
    UNKNOWN((short) 0),
    /**
     * 1 - Successful use with no errors detected.
     */
    SUCCESSFUL((short) 1),
    /**
     * 2 - Successful use with one or more errors detected.
     */
    SUCCESSFUL_W_ERROR((short) 2),
    /**
     * 3 - Unsuccessful use (e.g., aborted).
     */
    UNSUCCESSFUL((short) 3);

    private short id;

    /**
     * Creates the operation result with the given result ID.
     * @param id the result ID
     */
    private PaAttributeOperationLastResultEnum(final short id) {
        this.id = id;
    }

    /**
     * Returns the operation result ID.
     *
     * @return the result ID
     */
    public short id() {
        return this.id;
    }

    /**
     * Returns the operational result for the given result ID.
     *
     * @param id the result ID
     * @return a result
     */
    public static PaAttributeOperationLastResultEnum fromCode(final short id) {

        if (id == UNSUCCESSFUL.id) {
            return UNSUCCESSFUL;
        }

        if (id == SUCCESSFUL_W_ERROR.id) {
            return SUCCESSFUL_W_ERROR;
        }

        if (id == SUCCESSFUL.id) {
            return SUCCESSFUL;
        }

        return UNKNOWN;
    }
}
