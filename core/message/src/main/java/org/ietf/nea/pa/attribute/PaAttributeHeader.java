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
package org.ietf.nea.pa.attribute;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import org.ietf.nea.pa.attribute.enums.PaAttributeFlagEnum;

import de.hsbremen.tc.tnc.message.m.attribute.ImAttributeHeader;

/**
 * IETF RFC 5792 integrity measurement attribute header.
 *
 *
 */
public class PaAttributeHeader implements ImAttributeHeader {

    private final EnumSet<PaAttributeFlagEnum> flags; // 8 bit(s)
    private final long vendorId; // 24 bit(s)
    private final long type; // 32 bit(s)
    private final long length; // 32 bit(s) min value is 12 for the 12 bytes in
                               // this header

    /**
     * Creates the header with the given values.
     *
     * @param flags the attribute flags to set
     * @param vendorId the attribute vendor ID
     * @param type the attribute type ID
     * @param length the attribute length
     */
    PaAttributeHeader(final PaAttributeFlagEnum[] flags,
            final long vendorId, final long type,
            final long length) {
        if (flags.length > 0) {
            this.flags = EnumSet.copyOf(Arrays.asList(flags));
        } else {
            this.flags = EnumSet.noneOf(PaAttributeFlagEnum.class);
        }
        this.vendorId = vendorId;
        this.type = type;
        this.length = length;
    }

    /**
     * @return the flags
     */
    public Set<PaAttributeFlagEnum> getFlags() {
        return Collections.unmodifiableSet(flags);
    }

    @Override
    public long getVendorId() {
        return this.vendorId;
    }

    @Override
    public long getAttributeType() {
        return this.type;
    }

    @Override
    public long getLength() {
        return length;
    }

    @Override
    public String toString() {
        return "PaAttributeHeader [flags=" + this.flags + ", vendorId="
                + this.vendorId + ", type=" + this.type + ", length="
                + this.length + "]";
    }
    
    
}
