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
 * Enumeration of known integrity measurement port filter status values.
 *
 * @author Carl-Heinz Genzel
 *
 */
public enum PaAttributePortFilterStatus {
    /**
     * 0 - Allowed - The port is allowed.
     */
    ALLOWED(false),
    /**
     * 1 - Blocked - The port is blocked.
     */
    BLOCKED(true);

    private final boolean blocked;

    /**
     * Creates the port filter status with the given status value.
     * True -> blocked, false -> allowed.
     *
     * @param blocked the port filter status
     */
    private PaAttributePortFilterStatus(final boolean blocked) {
        this.blocked = blocked;
    }

    /**
     * Returns the port filter status as boolean.
     *
     * @return the port filter status
     */
    public boolean blocked() {
        return this.blocked;
    }

    /**
     * Returns the port filter status as bit value (e.g 0x0 or 0x1).
     *
     * @return the port filter status bit
     */
    public byte toStatusBit() {
        return (byte) (this.blocked ? 1 : 0);
    }

    /**
     * Returns the port filter status for the given bit value.
     *
     * @param bit the port filter status bit
     * @return a status or null
     */
    public static PaAttributePortFilterStatus fromBlockedBit(final byte bit) {

        if (bit == 0) {
            return ALLOWED;
        }

        if (bit == 1) {
            return BLOCKED;
        }

        return null;
    }
}
