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
 * @author Carl-Heinz Genzel
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
