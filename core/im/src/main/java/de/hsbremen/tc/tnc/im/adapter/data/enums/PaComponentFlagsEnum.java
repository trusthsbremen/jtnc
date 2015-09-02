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
