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
