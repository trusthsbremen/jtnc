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

import org.ietf.nea.pa.attribute.enums.PaAttributeRemediationParameterTypeEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;
import org.ietf.nea.pa.attribute.util.AbstractPaAttributeValueRemediationParameter;
import org.ietf.nea.pa.validate.rules.TypeReservedAndLimits;
import org.ietf.nea.pa.validate.rules.VendorIdReservedAndLimits;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.message.exception.RuleException;

public class PaAttributeValueRemediationParametersBuilderIetf implements PaAttributeValueRemediationParametersBuilder{
    
    private long rpVendorId;         // 24 bit(s)
    private long rpType;             // 32 bit(s)
    
    private long length; 
    
    private AbstractPaAttributeValueRemediationParameter parameter;
    
    public PaAttributeValueRemediationParametersBuilderIetf(){
    	this.rpVendorId = IETFConstants.IETF_PEN_VENDORID;
    	this.rpType = PaAttributeRemediationParameterTypeEnum.IETF_STRING.id();
    	this.length = PaAttributeTlvFixedLengthEnum.REM_PAR.length();
    	this.parameter = null;
    }

	@Override
	public PaAttributeValueRemediationParametersBuilder setRpVendorId(long rpVendorId) throws RuleException {
		
		VendorIdReservedAndLimits.check(rpVendorId);
		this.rpVendorId = rpVendorId;
	
		return this;
	}

	@Override
	public PaAttributeValueRemediationParametersBuilder setRpType(long rpType) throws RuleException {
		
		TypeReservedAndLimits.check(rpType);
		this.rpType = rpType;
		
		return this;
	}

	@Override
	public PaAttributeValueRemediationParametersBuilder setParameter(
			AbstractPaAttributeValueRemediationParameter parameter) {
		
		if(parameter != null){
			this.parameter = parameter;
			this.length = PaAttributeTlvFixedLengthEnum.REM_PAR.length() + parameter.getLength();
		}
		
		return this;
	}

	@Override
	public PaAttributeValueRemediationParameters toObject(){
		if(parameter == null){
			throw new IllegalStateException("A message value has to be set.");
		}
		
		return new PaAttributeValueRemediationParameters(this.rpVendorId, this.rpType, this.length, this.parameter);
	}

	@Override
	public PaAttributeValueRemediationParametersBuilder newInstance() {

		return new PaAttributeValueRemediationParametersBuilderIetf();
	}

}
