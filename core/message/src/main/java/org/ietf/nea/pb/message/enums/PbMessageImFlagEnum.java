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
package org.ietf.nea.pb.message.enums;

/**
 * Enumeration of known TNCCS message integrity measurement component value
 * flag bits. All bits together fill exactly one byte (111111111).
 *
 *
 */
public enum PbMessageImFlagEnum {

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
     * 10000000 - Exclusive - Indicates that the message value has to be
     * delivered to one exclusive recipient and not to all interested
     * recipients.
     */
    EXCL((byte) 0x80);

    private final byte bit;

    /**
     * Creates the flag with the given bit value.
     *
     * @param b the bit value
     */
    private PbMessageImFlagEnum(final byte b) {
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
    public static PbMessageImFlagEnum fromBit(final byte bit) {
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

        if (bit == EXCL.bit) {
            return EXCL;
        }

        return null;
    }
}
