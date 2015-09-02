/**
 * The BSD 3-Clause License ("BSD New" or "BSD Simplified")
 *
 * Copyright © 2015 Trust HS Bremen and its Contributors. All rights   
 * reserved.
 *
 * See the CONTRIBUTORS file distributed with this work for additional
 * information regarding copyright ownership.
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
