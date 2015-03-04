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
 * Enumeration of known integrity measurement factory default password status
 * values.
 *
 * @author Carl-Heinz Genzel
 *
 */
public enum PaAttributeFactoryDefaultPasswordStatusEnum {

    /**
     * 0 - Not Set - Endpoint does not have factory default password enabled.
     */
    NOT_SET(0),
    /**
     * 1 - Set - Endpoint has factory default password enabled.
     */
    SET(1);

    private long id;

    /**
     * Creates the factory default password status with the given status ID.
     * @param id the status ID
     */
    private PaAttributeFactoryDefaultPasswordStatusEnum(final long id) {
        this.id = id;
    }

    /**
     * Returns the factory default password status ID.
     *
     * @return the status ID
     */
    public long id() {
        return this.id;
    }

    /**
     * Returns the factory default password status for the given status ID.
     *
     * @param id the status ID
     * @return a status or null
     */
    public static PaAttributeFactoryDefaultPasswordStatusEnum fromId(
            final long id) {
        if (id == NOT_SET.id) {
            return NOT_SET;
        }
        if (id == SET.id) {
            return SET;
        }

        return null;
    }
}
