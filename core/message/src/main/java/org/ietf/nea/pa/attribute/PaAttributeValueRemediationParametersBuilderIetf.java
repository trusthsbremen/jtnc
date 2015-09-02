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

import org.ietf.nea.pa.attribute.enums.PaAttributeRemediationParameterTypeEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;
import org.ietf.nea.pa.attribute.util
.AbstractPaAttributeValueRemediationParameter;
import org.ietf.nea.pa.validate.rules.TypeReservedAndLimits;
import org.ietf.nea.pa.validate.rules.VendorIdReservedAndLimits;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.message.exception.RuleException;

/**
 * Builder to build an integrity measurement remediation parameters attribute
 * value compliant to RFC 5792. It evaluates the given values and can be used in
 * a fluent way.
 *
 *
 */
public class PaAttributeValueRemediationParametersBuilderIetf implements
        PaAttributeValueRemediationParametersBuilder {

    private long rpVendorId; // 24 bit(s)
    private long rpType; // 32 bit(s)

    private long length;

    private AbstractPaAttributeValueRemediationParameter parameter;

    /**
     * Creates the builder using default values.
     * <ul>
     * <li>Length: Fixed value length only</li>
     * <li>Vendor: IETF</li>
     * <li>Type: String</li>
     * <li>Parameter: null</li>
     * </ul>
     */
    public PaAttributeValueRemediationParametersBuilderIetf() {
        this.rpVendorId = IETFConstants.IETF_PEN_VENDORID;
        this.rpType = PaAttributeRemediationParameterTypeEnum.IETF_STRING.id();
        this.length = PaAttributeTlvFixedLengthEnum.REM_PAR.length();
        this.parameter = null;
    }

    @Override
    public PaAttributeValueRemediationParametersBuilder setRpVendorId(
            final long rpVendorId) throws RuleException {

        VendorIdReservedAndLimits.check(rpVendorId);
        this.rpVendorId = rpVendorId;

        return this;
    }

    @Override
    public PaAttributeValueRemediationParametersBuilder setRpType(
            final long rpType) throws RuleException {

        TypeReservedAndLimits.check(rpType);
        this.rpType = rpType;

        return this;
    }

    @Override
    public PaAttributeValueRemediationParametersBuilder setParameter(
            final AbstractPaAttributeValueRemediationParameter parameter) {

        if (parameter != null) {
            this.parameter = parameter;
            this.length = PaAttributeTlvFixedLengthEnum.REM_PAR.length()
                    + parameter.getLength();
        }

        return this;
    }

    @Override
    public PaAttributeValueRemediationParameters toObject() {
        if (parameter == null) {
            throw new IllegalStateException("A message value has to be set.");
        }

        return new PaAttributeValueRemediationParameters(this.rpVendorId,
                this.rpType, this.length, this.parameter);
    }

    @Override
    public PaAttributeValueRemediationParametersBuilder newInstance() {

        return new PaAttributeValueRemediationParametersBuilderIetf();
    }

}
