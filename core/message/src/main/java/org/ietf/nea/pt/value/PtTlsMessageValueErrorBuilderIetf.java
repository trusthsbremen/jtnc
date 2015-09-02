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
