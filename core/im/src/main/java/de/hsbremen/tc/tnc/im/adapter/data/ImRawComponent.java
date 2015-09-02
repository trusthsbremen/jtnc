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
package de.hsbremen.tc.tnc.im.adapter.data;

import java.util.Arrays;

/**
 * Holds a message concerning an integrity measurement
 * component including the message header values and
 * the message as raw byte array.
 *
 *
 */
public class ImRawComponent extends AbstractImComponent {

    private final byte imFlags; // 8 bit(s)

    private final byte[] message;

    /**
     * Creates a component with the necessary address attributes and
     * the message as raw byte array.
     *
     * @param flags the message flags as composed byte
     * @param vendorId the vendor ID describing the component
     * @param type the type ID describing the component
     * @param collectorId the referred IMC
     * @param validatorId the referred IMV
     * @param message the raw message
     */
    ImRawComponent(final byte flags,
            final long vendorId, final long type,
            final long collectorId, final long validatorId,
            final byte[] message) {
        super(vendorId, type, collectorId, validatorId);

        this.imFlags = flags;
        this.message = message;
    }

    /**
     * Returns the flags value.
     * @return the flags value
     */
    public byte getImFlags() {
        return this.imFlags;
    }

    /**
     * Returns a copy of the raw message as byte array.
     * @return the raw message
     */
    public byte[] getMessage() {
        return Arrays.copyOf(message, message.length);
    }

}
