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
package de.hsbremen.tc.tnc.im.adapter.data.enums;

/**
 * Enumeration of known component message flags.
 *
 *
 */
public enum PaComponentFlagsEnum {

    /**
     * Last bit. Reserved, not for use.
     */
    RESERVED1((byte) 0x01),
    /**
     * Reserved, not for use.
     */
    RESERVED2((byte) 0x02),
    /**
     * Reserved, not for use.
     */
    RESERVED3((byte) 0x04),
    /**
     * Reserved, not for use.
     */
    RESERVED4((byte) 0x08),
    /**
     * Reserved, not for use.
     */
    RESERVED5((byte) 0x10),
    /**
     * Reserved, not for use.
     */
    RESERVED6((byte) 0x20),
    /**
     * Reserved, not for use.
     */
    RESERVED7((byte) 0x40),
    /**
     * First bit. Exclusive bit, to set for exclusive delivery.
     */
    EXCL((byte) 0x80);

    private final byte bit;

    /**
     * Creates a flag enumeration value with a flag bit.
     * @param b the flag bit
     */
    private PaComponentFlagsEnum(final byte b) {
        this.bit = b;
    }

    /**
     * Returns the flag bit.
     * @return the flag bit
     */
    public final byte bit() {
        return bit;
    }
}
