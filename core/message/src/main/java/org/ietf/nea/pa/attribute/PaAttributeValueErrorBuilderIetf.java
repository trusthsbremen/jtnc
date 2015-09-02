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
package org.ietf.nea.pa.attribute;

import org.ietf.nea.pa.attribute.enums.PaAttributeErrorCodeEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;
import org.ietf.nea.pa.attribute.util.AbstractPaAttributeValueErrorInformation;
import org.ietf.nea.pa.validate.rules.TypeReservedAndLimits;
import org.ietf.nea.pa.validate.rules.VendorIdReservedAndLimits;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.message.exception.RuleException;

/**
 * Builder to build an integrity measurement error attribute value compliant to
 * RFC 5792. It evaluates the given values and can be used in a fluent way.
 *
 *
 */
public class PaAttributeValueErrorBuilderIetf implements
        PaAttributeValueErrorBuilder {

    private long errorVendorId; // 24 bit(s)
    private long errorCode; // 32 bit(s)

    private long length;

    private AbstractPaAttributeValueErrorInformation errorInformation;

    /**
     * Creates the builder using default values.
     * <ul>
     * <li>Length: Fixed value length only</li>
     * <li>Vendor: IETF</li>
     * <li>Code: Invalid parameter</li>
     * <li>Information: null</li>
     * </ul>
     */
    public PaAttributeValueErrorBuilderIetf() {
        this.errorVendorId = IETFConstants.IETF_PEN_VENDORID;
        this.errorCode = PaAttributeErrorCodeEnum.IETF_INVALID_PARAMETER.code();
        this.length = PaAttributeTlvFixedLengthEnum.ERR_INF.length();
        this.errorInformation = null;
    }

    @Override
    public PaAttributeValueErrorBuilder setErrorVendorId(final long rpVendorId)
            throws RuleException {

        VendorIdReservedAndLimits.check(rpVendorId);
        this.errorVendorId = rpVendorId;

        return this;
    }

    @Override
    public PaAttributeValueErrorBuilder setErrorCode(final long rpType)
            throws RuleException {

        TypeReservedAndLimits.check(rpType);
        this.errorCode = rpType;

        return this;
    }

    @Override
    public PaAttributeValueErrorBuilder setErrorInformation(
            final AbstractPaAttributeValueErrorInformation parameter) {

        if (parameter != null) {
            this.errorInformation = parameter;
            this.length = PaAttributeTlvFixedLengthEnum.ERR_INF.length()
                    + parameter.getLength();
        }

        return this;
    }

    @Override
    public PaAttributeValueError toObject() {
        if (errorInformation == null) {
            throw new IllegalStateException(
                    "An error information has to be set.");
        }

        return new PaAttributeValueError(this.length, this.errorVendorId,
                this.errorCode, this.errorInformation);
    }

    @Override
    public PaAttributeValueErrorBuilder newInstance() {

        return new PaAttributeValueErrorBuilderIetf();
    }

}
