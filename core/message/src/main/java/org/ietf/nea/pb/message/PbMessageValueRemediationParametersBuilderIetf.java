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

import org.ietf.nea.pb.message.enums.PbMessageRemediationParameterTypeEnum;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLengthEnum;
import org.ietf.nea.pb.message.util.AbstractPbMessageValueRemediationParameter;
import org.ietf.nea.pb.validate.rules.TypeReservedAndLimits;
import org.ietf.nea.pb.validate.rules.VendorIdReservedAndLimits;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.message.exception.RuleException;

public class PbMessageValueRemediationParametersBuilderIetf implements PbMessageValueRemediationParametersBuilder{
    
    private long rpVendorId;         // 24 bit(s)
    private long rpType;             // 32 bit(s)
    
    private long length; 
    
    private AbstractPbMessageValueRemediationParameter parameter;
    
    public PbMessageValueRemediationParametersBuilderIetf(){
    	this.rpVendorId = IETFConstants.IETF_PEN_VENDORID;
    	this.rpType = PbMessageRemediationParameterTypeEnum.IETF_STRING.id();
    	this.length = PbMessageTlvFixedLengthEnum.REM_PAR_VALUE.length();
    	this.parameter = null;
    }

	@Override
	public PbMessageValueRemediationParametersBuilder setRpVendorId(long rpVendorId) throws RuleException {
		
		VendorIdReservedAndLimits.check(rpVendorId);
		this.rpVendorId = rpVendorId;
	
		return this;
	}

	@Override
	public PbMessageValueRemediationParametersBuilder setRpType(long rpType) throws RuleException {
		
		TypeReservedAndLimits.check(rpType);
		this.rpType = rpType;
		
		return this;
	}

	@Override
	public PbMessageValueRemediationParametersBuilder setParameter(
			AbstractPbMessageValueRemediationParameter parameter) {
		
		if(parameter != null){
			this.parameter = parameter;
			this.length = PbMessageTlvFixedLengthEnum.REM_PAR_VALUE.length() + parameter.getLength();
		}
		
		return this;
	}

	@Override
	public PbMessageValueRemediationParameters toObject(){
		if(parameter == null){
			throw new IllegalStateException("A remediation value has to be set.");
		}
		
		return new PbMessageValueRemediationParameters(this.rpVendorId, this.rpType, this.length, this.parameter);
	}

	@Override
	public PbMessageValueRemediationParametersBuilder newInstance() {

		return new PbMessageValueRemediationParametersBuilderIetf();
	}

}
