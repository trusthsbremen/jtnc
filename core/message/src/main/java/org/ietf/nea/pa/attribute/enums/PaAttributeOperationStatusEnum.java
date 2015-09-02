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
 * Enumeration of known integrity measurement operational status values.
 *
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
