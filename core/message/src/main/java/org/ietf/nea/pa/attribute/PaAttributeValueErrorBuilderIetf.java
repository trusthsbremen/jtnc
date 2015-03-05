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
 * @author Carl-Heinz Genzel
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
