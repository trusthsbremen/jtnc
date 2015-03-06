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

import org.ietf.nea.pt.message.enums.PtTlsMessageTlvFixedLengthEnum;
import org.ietf.nea.pt.message.enums.PtTlsMessageTypeEnum;
import org.ietf.nea.pt.validate.rules.CommonLengthLimits;
import org.ietf.nea.pt.validate.rules.IdentifierLimits;
import org.ietf.nea.pt.validate.rules.TypeReservedAndLimits;
import org.ietf.nea.pt.validate.rules.VendorIdReservedAndLimits;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.message.exception.RuleException;

/**
 * Builder to build a transport message header compliant to RFC 6876. It
 * evaluates the given values and can be used in a fluent way.
 *
 *
 */
public class PtTlsMessageHeaderBuilderIetf implements
    PtTlsMessageHeaderBuilder {

    private long vendorId;
    private long type;
    private long length;
    private long identifier;

    /**
     * Creates the builder using default values.
     * <ul>
     * <li>Vendor: IETF</li>
     * <li>Type: TNCCS batch </li>
     * <li>Length: Header length only</li>
     * </ul>
     */
    public PtTlsMessageHeaderBuilderIetf() {
        this.vendorId = IETFConstants.IETF_PEN_VENDORID;
        this.type = PtTlsMessageTypeEnum.IETF_PT_TLS_PB_BATCH.id();
        this.length = PtTlsMessageTlvFixedLengthEnum.MESSAGE.length();
    }

    @Override
    public PtTlsMessageHeaderBuilder setVendorId(final long vendorId)
            throws RuleException {

        VendorIdReservedAndLimits.check(vendorId);
        this.vendorId = vendorId;

        return this;
    }

    @Override
    public PtTlsMessageHeaderBuilder setType(final long type)
            throws RuleException {

        TypeReservedAndLimits.check(type);
        this.type = type;

        return this;
    }

    @Override
    public PtTlsMessageHeaderBuilder setLength(final long length)
            throws RuleException {

        CommonLengthLimits.check(length);
        this.length = length;

        return this;
    }

    @Override
    public PtTlsMessageHeaderBuilder setIdentifier(final long identifier)
            throws RuleException {

        IdentifierLimits.check(identifier);
        this.identifier = identifier;

        return this;
    }

    @Override
    public PtTlsMessageHeader toObject() {

        PtTlsMessageHeader mHead = new PtTlsMessageHeader(this.vendorId,
                this.type, this.length, this.identifier);

        return mHead;
    }

    @Override
    public PtTlsMessageHeaderBuilder newInstance() {
        return new PtTlsMessageHeaderBuilderIetf();
    }

}
