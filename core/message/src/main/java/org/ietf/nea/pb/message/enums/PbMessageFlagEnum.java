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
 * Enumeration of known TNCCS message flag bits. All bits together fill exactly
 * one byte (111111111).
 *
 * @author Carl-Heinz Genzel
 *
 */
public enum PbMessageFlagEnum {

    /**
     * 00000001 - Reserved - Not used.
     */
    RESERVED1((byte) 0x01),
    /**
     * 00000010 - Reserved - Not used.
     */
    RESERVED2((byte) 0x02),
    /**
     * 00000100 - Reserved - Not used.
     */
    RESERVED3((byte) 0x04),
    /**
     * 00001000 - Reserved - Not used.
     */
    RESERVED4((byte) 0x08),
    /**
     * 00010000 - Reserved - Not used.
     */
    RESERVED5((byte) 0x10),
    /**
     * 00100000 - Reserved - Not used.
     */
    RESERVED6((byte) 0x20),
    /**
     * 01000000 - Reserved - Not used.
     */
    RESERVED7((byte) 0x40),
    /**
     * 10000000 - No skip - Indicates that the message is mandatory and cannot
     * be ignored.
     */
    NOSKIP((byte) 0x80);

    private final byte bit;

    /**
     * Creates the flag with the given bit value.
     *
     * @param b the bit value
     */
    private PbMessageFlagEnum(final byte b) {
        this.bit = b;
    }

    /**
     * Returns the flag bit.
     *
     * @return the flag bit
     */
    public final byte bit() {
        return bit;
    }

    /**
     * Returns the flag for the given bit value.
     *
     * @param bit the bit value
     * @return a flag or null
     */
    public static PbMessageFlagEnum fromBit(final byte bit) {
        if (bit == RESERVED1.bit) {
            return RESERVED1;
        }

        if (bit == RESERVED2.bit) {
            return RESERVED2;
        }

        if (bit == RESERVED3.bit) {
            return RESERVED3;
        }

        if (bit == RESERVED4.bit) {
            return RESERVED4;
        }

        if (bit == RESERVED5.bit) {
            return RESERVED5;
        }

        if (bit == RESERVED6.bit) {
            return RESERVED6;
        }

        if (bit == RESERVED7.bit) {
            return RESERVED7;
        }

        if (bit == NOSKIP.bit) {
            return NOSKIP;
        }

        return null;
    }
}
