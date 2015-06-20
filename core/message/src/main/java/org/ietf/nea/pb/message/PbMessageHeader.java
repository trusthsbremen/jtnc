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
package org.ietf.nea.pb.message;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import org.ietf.nea.pb.message.enums.PbMessageFlagEnum;

import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessageHeader;

/**
 * IETF RFC 5793 TNCCS message header.
 *
 *
 */
public class PbMessageHeader implements TnccsMessageHeader {

    private final EnumSet<PbMessageFlagEnum> flags; // 8 bit(s)
    private final long vendorId; // 24 bit(s)
    private final long type; // 32 bit(s)
    private final long length; // 32 bit(s) min value is 12 for the 12 bytes in
                               // this header

    /**
     * Creates the header with the given values.
     *
     * @param flags the message flags to set
     * @param vendorId the message vendor ID
     * @param type the message type ID
     * @param length the message length
     */
    PbMessageHeader(final PbMessageFlagEnum[] flags, final long vendorId,
            final long type, final long length) {
        if (flags.length > 0) {
            this.flags = EnumSet.copyOf(Arrays.asList(flags));
        } else {
            this.flags = EnumSet.noneOf(PbMessageFlagEnum.class);
        }
        this.vendorId = vendorId;
        this.type = type;
        this.length = length;
    }

    /**
     * Returns the set of used message flags.
     * @return the message flags
     */
    public Set<PbMessageFlagEnum> getFlags() {
        return Collections.unmodifiableSet(flags);
    }

    @Override
    public long getVendorId() {
        return vendorId;
    }

    @Override
    public long getMessageType() {
        return type;
    }

    @Override
    public long getLength() {
        return length;
    }

    @Override
    public String toString() {
        return "PbMessageHeader [flags=" + this.flags + ", vendorId="
                + this.vendorId + ", type=" + this.type + ", length="
                + this.length + "]";
    }

}
