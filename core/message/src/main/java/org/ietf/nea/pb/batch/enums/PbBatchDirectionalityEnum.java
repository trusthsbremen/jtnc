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
package org.ietf.nea.pb.batch.enums;

/**
 * Enumeration of known batch directions.
 *
 *
 */
public enum PbBatchDirectionalityEnum {
    /**
     * Batch directed to client.
     */
    TO_PBC(false),
    /**
     * Batch directed to server.
     */
    TO_PBS(true);

    private final boolean directionality;

    /**
     * Creates the direction with the given directionality.
     * True -> to server, false -> to client.
     *
     * @param directionality the directionality
     */
    private PbBatchDirectionalityEnum(final boolean directionality) {
        this.directionality = directionality;
    }

    /**
     * Returns the directionality as boolean.
     * @return the directionality
     */
    public boolean directionality() {
        return this.directionality;
    }

    /**
     * Returns the directionality as bit value (e.g. 0x0 or 0x1).
     * @return the directionality bit
     */
    public byte toDirectionalityBit() {
        return (byte) (this.directionality ? 0 : 1);
    }

    /**
     * Return the directionality for the given bit value.
     * @param directionality the directionality bit
     * @return a direction or null
     */
    public static PbBatchDirectionalityEnum fromDirectionalityBit(
            final byte directionality) {

        if (directionality == 1) {
            return TO_PBC;
        }

        if (directionality == 0) {
            return TO_PBS;
        }

        return null;
    }
}
