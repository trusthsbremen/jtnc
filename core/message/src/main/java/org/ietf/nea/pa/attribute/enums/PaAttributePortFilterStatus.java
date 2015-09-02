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
 * Enumeration of known integrity measurement port filter status values.
 *
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
