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

import org.ietf.nea.pa.attribute.enums.PaAttributeFlagEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeTypeEnum;
import org.ietf.nea.pa.validate.rules.CommonLengthLimits;
import org.ietf.nea.pa.validate.rules.TypeReservedAndLimits;
import org.ietf.nea.pa.validate.rules.VendorIdReservedAndLimits;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.message.exception.RuleException;

/**
 * Builder to build an integrity measurement attribute header compliant
 * to RFC 5792. It evaluates the given values and can be used in a fluent way.
 *
 *
 */
public class PaAttributeHeaderBuilderIetf implements PaAttributeHeaderBuilder {

    private PaAttributeFlagEnum[] flags;
    private long vendorId;
    private long type;
    private long length;

    /**
     * Creates the builder using default values.
     * <ul>
     * <li>Flags: No flags set</li>
     * <li>Vendor: IETF</li>
     * <li>Type: Testing </li>
     * <li>Length: Header length only</li>
     * </ul>
     */
    public PaAttributeHeaderBuilderIetf() {
        this.flags = new PaAttributeFlagEnum[0];
        this.vendorId = IETFConstants.IETF_PEN_VENDORID;
        this.type = PaAttributeTypeEnum.IETF_PA_TESTING
                .id();
        this.length = PaAttributeTlvFixedLengthEnum.ATTRIBUTE.length();
    }

    @Override
    public PaAttributeHeaderBuilder setFlags(final byte flags) {
        if ((byte) (flags & 0x80) == PaAttributeFlagEnum.NOSKIP.bit()) {
            this.flags =
                    new PaAttributeFlagEnum[] {PaAttributeFlagEnum.NOSKIP};
        }

        return this;
    }

    @Override
    public PaAttributeHeaderBuilder setVendorId(final long vendorId)
            throws RuleException {

        VendorIdReservedAndLimits.check(vendorId);
        this.vendorId = vendorId;

        return this;
    }

    @Override
    public PaAttributeHeaderBuilder setType(final long type)
            throws RuleException {

        TypeReservedAndLimits.check(type);
        this.type = type;

        return this;
    }

    @Override
    public PaAttributeHeaderBuilder setLength(final long length)
            throws RuleException {

        CommonLengthLimits.check(length);
        this.length = length;

        return this;
    }

    @Override
    public PaAttributeHeader toObject() {

        PaAttributeHeader mHead = new PaAttributeHeader(this.flags,
                this.vendorId, this.type, this.length);

        return mHead;
    }

    @Override
    public PaAttributeHeaderBuilder newInstance() {

        return new PaAttributeHeaderBuilderIetf();
    }
}
