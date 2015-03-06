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

import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.message.enums.PbMessageErrorFlagsEnum;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLengthEnum;
import org.ietf.nea.pb.message.util.AbstractPbMessageValueErrorParameter;
import org.ietf.nea.pb.validate.rules.ErrorCodeLimits;
import org.ietf.nea.pb.validate.rules.ErrorVendorIdLimits;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.message.exception.RuleException;

/**
 * Builder to build a TNCCS error message value compliant to RFC 5793. It
 * evaluates the given values and can be used in a fluent way.
 *
 *
 */
public class PbMessageValueErrorBuilderIetf implements
        PbMessageValueErrorBuilder {

    private PbMessageErrorFlagsEnum[] errorFlags; // 8 bit(s)
    private long errorVendorId; // 24 bit(s)
    private int errorCode; // 16 bit(s)
    private long length;
    private AbstractPbMessageValueErrorParameter errorParameter; // 32 bit(s)

    /**
     * Creates the builder using default values.
     * <ul>
     * <li>Flags: None set</li>
     * <li>Vendor: IETF</li>
     * <li>Code: Local error</li>
     * <li>Parameter: null</li>
     * <li>Length: Fixed value length only</li>
     * </ul>
     */
    public PbMessageValueErrorBuilderIetf() {
        this.errorFlags = new PbMessageErrorFlagsEnum[0];
        this.errorVendorId = IETFConstants.IETF_PEN_VENDORID;
        this.errorCode = PbMessageErrorCodeEnum.IETF_LOCAL.code();
        this.length = PbMessageTlvFixedLengthEnum.ERR_VALUE.length();
        this.errorParameter = null;
    }

    @Override
    public PbMessageValueErrorBuilder setErrorFlags(final byte errorFlags) {
        // filter for FATAL
        if ((byte) (errorFlags & PbMessageErrorFlagsEnum.FATAL.bit())
                == PbMessageErrorFlagsEnum.FATAL.bit()) {
            this.errorFlags = new PbMessageErrorFlagsEnum[] {
                    PbMessageErrorFlagsEnum.FATAL};
        }

        return this;
    }

    @Override
    public PbMessageValueErrorBuilder setErrorVendorId(final long errorVendorId)
            throws RuleException {

        ErrorVendorIdLimits.check(errorVendorId);
        this.errorVendorId = errorVendorId;

        return this;
    }

    @Override
    public PbMessageValueErrorBuilder setErrorCode(final int errorCode)
            throws RuleException {

        ErrorCodeLimits.check(errorCode);
        this.errorCode = errorCode;

        return this;
    }

    @Override
    public PbMessageValueErrorBuilder setErrorParameter(
            final AbstractPbMessageValueErrorParameter errorParameter) {

        if (errorParameter != null) {
            this.errorParameter = errorParameter;
            this.length = PbMessageTlvFixedLengthEnum.ERR_VALUE.length()
                    + errorParameter.getLength();
        }

        return this;
    }

    @Override
    public PbMessageValueError toObject() {

        return new PbMessageValueError(this.errorFlags, this.errorVendorId,
                this.errorCode, this.length, this.errorParameter);
    }

    @Override
    public PbMessageValueErrorBuilder newInstance() {

        return new PbMessageValueErrorBuilderIetf();
    }

}
