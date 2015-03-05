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
package org.ietf.nea.pt.value;

import org.ietf.nea.pt.message.enums.PtTlsMessageTlvFixedLengthEnum;
import org.ietf.nea.pt.validate.rules.TypeReservedAndLimits;
import org.ietf.nea.pt.validate.rules.VendorIdReservedAndLimits;
import org.ietf.nea.pt.value.enums.PtTlsMessageErrorCodeEnum;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.message.exception.RuleException;

/**
 * Builder to build a transport error message value compliant to RFC 6876. It
 * evaluates the given values and can be used in a fluent way.
 *
 * @author Carl-Heinz Genzel
 *
 */
public class PtTlsMessageValueErrorBuilderIetf implements
        PtTlsMessageValueErrorBuilder {

    private long errorVendorId; // 24 bit(s)
    private long errorCode; // 32 bit(s)
    private long length;
    private byte[] message;

    /**
     * Creates the builder using default values.
     * <ul>
     * <li>Length: Fixed value length only</li>
     * <li>Vendor: IETF</li>
     * <li>Code: Reserved</li>
     * <li>Message: null</li>
     * </ul>
     */
    public PtTlsMessageValueErrorBuilderIetf() {
        this.errorVendorId = IETFConstants.IETF_PEN_VENDORID;
        this.errorCode = PtTlsMessageErrorCodeEnum.IETF_RESERVED.code();
        this.length = PtTlsMessageTlvFixedLengthEnum.ERR_VALUE.length();
        this.message = null;
    }

    @Override
    public PtTlsMessageValueErrorBuilder setErrorVendorId(
            final long errorVendorId) throws RuleException {

        VendorIdReservedAndLimits.check(errorVendorId);
        this.errorVendorId = errorVendorId;

        return this;
    }

    @Override
    public PtTlsMessageValueErrorBuilder setErrorCode(final long errorCode)
            throws RuleException {

        TypeReservedAndLimits.check(errorCode);
        this.errorCode = errorCode;

        return this;
    }

    @Override
    public PtTlsMessageValueErrorBuilder setPartialMessage(
            final byte[] message) {

        if (message != null) {
            this.message = message;
            this.length = PtTlsMessageTlvFixedLengthEnum.ERR_VALUE.length()
                    + message.length;
        }

        return this;
    }

    @Override
    public PtTlsMessageValueError toObject() {
        if (this.message == null) {
            throw new IllegalStateException(
                    "The faulty message has to be copied and set.");
        }

        return new PtTlsMessageValueError(this.errorVendorId, this.errorCode,
                this.length, this.message);
    }

    @Override
    public PtTlsMessageValueErrorBuilder newInstance() {

        return new PtTlsMessageValueErrorBuilderIetf();
    }

}
