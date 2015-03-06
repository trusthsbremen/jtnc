/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Carl-Heinz Genzel
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
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
}
