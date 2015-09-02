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

import org.ietf.nea.pb.message.enums.PbMessageRemediationParameterTypeEnum;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLengthEnum;
import org.ietf.nea.pb.message.util.AbstractPbMessageValueRemediationParameter;
import org.ietf.nea.pb.validate.rules.TypeReservedAndLimits;
import org.ietf.nea.pb.validate.rules.VendorIdReservedAndLimits;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.message.exception.RuleException;

/**
 * Builder to build a TNCCS remediation parameters message value compliant to
 * RFC 5793. It evaluates the given values and can be used in a fluent way.
 *
 *
 */
public class PbMessageValueRemediationParametersBuilderIetf implements
        PbMessageValueRemediationParametersBuilder {

    private long rpVendorId; // 24 bit(s)
    private long rpType; // 32 bit(s)

    private long length;

    private AbstractPbMessageValueRemediationParameter parameter;

    /**
     * Creates the builder using default values.
     * <ul>
     * <li>Length: Fixed value length only</li>
     * <li>Vendor: IETF</li>
     * <li>Type: String</li>
     * <li>Parameter: null</li>
     * </ul>
     */
    public PbMessageValueRemediationParametersBuilderIetf() {
        this.rpVendorId = IETFConstants.IETF_PEN_VENDORID;
        this.rpType = PbMessageRemediationParameterTypeEnum.IETF_STRING.id();
        this.length = PbMessageTlvFixedLengthEnum.REM_PAR_VALUE.length();
        this.parameter = null;
    }

    @Override
    public PbMessageValueRemediationParametersBuilder setRpVendorId(
            final long rpVendorId) throws RuleException {

        VendorIdReservedAndLimits.check(rpVendorId);
        this.rpVendorId = rpVendorId;

        return this;
    }

    @Override
    public PbMessageValueRemediationParametersBuilder setRpType(
            final long rpType) throws RuleException {

        TypeReservedAndLimits.check(rpType);
        this.rpType = rpType;

        return this;
    }

    @Override
    public PbMessageValueRemediationParametersBuilder setParameter(
            final AbstractPbMessageValueRemediationParameter parameter) {

        if (parameter != null) {
            this.parameter = parameter;
            this.length = PbMessageTlvFixedLengthEnum.REM_PAR_VALUE.length()
                    + parameter.getLength();
        }

        return this;
    }

    @Override
    public PbMessageValueRemediationParameters toObject() {
        if (parameter == null) {
            throw new IllegalStateException(
                    "A remediation value has to be set.");
        }

        return new PbMessageValueRemediationParameters(this.rpVendorId,
                this.rpType, this.length, this.parameter);
    }

    @Override
    public PbMessageValueRemediationParametersBuilder newInstance() {

        return new PbMessageValueRemediationParametersBuilderIetf();
    }

}
