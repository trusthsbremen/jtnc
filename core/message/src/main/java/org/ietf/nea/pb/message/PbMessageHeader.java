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
 * @author Carl-Heinz Genzel
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
     * @return the flags
     */
    public Set<PbMessageFlagEnum> getFlags() {
        return Collections.unmodifiableSet(flags);
    }

    /**
     * @return the vendorId
     */
    @Override
    public long getVendorId() {
        return vendorId;
    }

    /**
     * @return the type
     */
    @Override
    public long getMessageType() {
        return type;
    }

    @Override
    public long getLength() {
        return length;
    }

}
