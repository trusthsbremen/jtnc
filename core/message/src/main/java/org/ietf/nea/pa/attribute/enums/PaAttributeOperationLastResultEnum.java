/**
 * The BSD 3-Clause License ("BSD New" or "BSD Simplified")
 *
 * Copyright (c) 2015, Carl-Heinz Genzel
 * All rights reserved.
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
