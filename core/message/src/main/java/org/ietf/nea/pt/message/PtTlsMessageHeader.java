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
package org.ietf.nea.pt.message;

import de.hsbremen.tc.tnc.message.t.message.TransportMessageHeader;

/**
 * IETF RFC 6876 transport message header.
 *
 *
 */
public class PtTlsMessageHeader implements TransportMessageHeader {

    private final long vendorId; // 24 bit(s)
    private final long type; // 32 bit(s)
    private final long length; // 32 bit(s) min value is 16 for the 16 bytes in
                               // this header
    private final long identifier; // 32 bit(s)

    /**
     * Creates the header with the given values.
     * @param vendorId the vendor ID
     * @param type the message type
     * @param length the message length
     * @param identifier the message identifier
     */
    public PtTlsMessageHeader(final long vendorId, final long type,
            final long length, final long identifier) {
        this.vendorId = vendorId;
        this.type = type;
        this.length = length;
        this.identifier = identifier;
    }

    /**
     * Returns the message vendor ID.
     * @return the vendor ID
     */
    public long getVendorId() {
        return vendorId;
    }

    /**
     * Returns the message type ID.
     * @return the message type ID
     */
    public long getMessageType() {
        return type;
    }

    @Override
    public long getLength() {
        return length;
    }

    @Override
    public long getIdentifier() {
        return this.identifier;
    }

    @Override
    public String toString() {
        return "PtTlsMessageHeader [vendorId=" + this.vendorId + ", type="
                + this.type + ", length=" + this.length + ", identifier="
                + this.identifier + "]";
    }
    
    

}
