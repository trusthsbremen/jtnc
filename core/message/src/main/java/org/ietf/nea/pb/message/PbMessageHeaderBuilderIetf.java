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
 * @author Carl-Heinz Genzel
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
