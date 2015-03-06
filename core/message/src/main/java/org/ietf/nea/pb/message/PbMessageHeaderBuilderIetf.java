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

import org.ietf.nea.pb.message.enums.PbMessageFlagEnum;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLengthEnum;
import org.ietf.nea.pb.message.enums.PbMessageTypeEnum;
import org.ietf.nea.pb.validate.rules.CommonLengthLimits;
import org.ietf.nea.pb.validate.rules.TypeReservedAndLimits;
import org.ietf.nea.pb.validate.rules.VendorIdReservedAndLimits;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.message.exception.RuleException;

/**
 * Builder to build a TNCCS message header compliant to RFC 5793. It
 * evaluates the given values and can be used in a fluent way.
 *
 *
 */
public class PbMessageHeaderBuilderIetf implements PbMessageHeaderBuilder {

    private PbMessageFlagEnum[] flags;
    private long vendorId;
    private long type;
    private long length;

    /**
     * Creates the builder using default values.
     * <ul>
     * <li>Vendor: IETF</li>
     * <li>Type: Language preference</li>
     * <li>Length: Header length only</li>
     * </ul>
     */
    public PbMessageHeaderBuilderIetf() {
        this.flags = new PbMessageFlagEnum[0];
        this.vendorId = IETFConstants.IETF_PEN_VENDORID;
        this.type = PbMessageTypeEnum.IETF_PB_LANGUAGE_PREFERENCE.id();
        this.length = PbMessageTlvFixedLengthEnum.MESSAGE.length();
    }

    @Override
    public PbMessageHeaderBuilder setFlags(final byte flags) {
        if ((byte) (flags & 0x80) == PbMessageFlagEnum.NOSKIP.bit()) {
            this.flags = new PbMessageFlagEnum[] {PbMessageFlagEnum.NOSKIP};
        }

        return this;
    }

    @Override
    public PbMessageHeaderBuilder setVendorId(final long vendorId)
            throws RuleException {

        VendorIdReservedAndLimits.check(vendorId);
        this.vendorId = vendorId;

        return this;
    }

    @Override
    public PbMessageHeaderBuilder setType(final long type)
            throws RuleException {

        TypeReservedAndLimits.check(type);
        this.type = type;

        return this;
    }

    @Override
    public PbMessageHeaderBuilder setLength(final long length)
            throws RuleException {

        CommonLengthLimits.check(length);
        this.length = length;

        return this;
    }

    @Override
    public PbMessageHeader toObject() {

        PbMessageHeader mHead = new PbMessageHeader(this.flags, this.vendorId,
                this.type, this.length);

        return mHead;
    }

    @Override
    public PbMessageHeaderBuilder newInstance() {
        return new PbMessageHeaderBuilderIetf();
    }
}
