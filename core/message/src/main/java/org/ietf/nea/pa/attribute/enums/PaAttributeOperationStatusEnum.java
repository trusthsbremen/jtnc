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
 * Enumeration of known integrity measurement operational status values.
 *
 * @author Carl-Heinz Genzel
 *
 */
public enum PaAttributeOperationStatusEnum {

    /**
     * 0 - Unknown or other - The status of the product is unknown.
     */
    UNKNOWN((short) 0),
    /**
     * 1 - Not installed - The product is not installed.
     */
    NOT_INSTALLED((short) 1),
    /**
     * 2 - Installed but not operational - The product is not operational.
     */
    INSTALLED_NOT_OPERATIONAL((short) 2),
    /**
     * 3 - Operational - The product is running.
     */
    OPERATIONAL((short) 3);

    private short id;

    /**
     * Creates the operational status with the given status ID.
     *
     * @param id the status ID
     */
    private PaAttributeOperationStatusEnum(final short id) {
        this.id = id;
    }

    /**
     * Returns the operationl status ID.
     *
     * @return the status ID
     */
    public short id() {
        return this.id;
    }

    /**
     * Returns the operational status for the given status ID.
     *
     * @param id the status ID
     * @return a status
     */
    public static PaAttributeOperationStatusEnum fromId(final short id) {

        if (id == OPERATIONAL.id) {
            return OPERATIONAL;
        }

        if (id == INSTALLED_NOT_OPERATIONAL.id) {
            return INSTALLED_NOT_OPERATIONAL;
        }

        if (id == NOT_INSTALLED.id) {
            return NOT_INSTALLED;
        }

        return UNKNOWN;
    }

}
