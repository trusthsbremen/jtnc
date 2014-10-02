package org.ietf.nea.pb.message;

import org.ietf.nea.pb.exception.RuleException;
import org.ietf.nea.pb.message.enums.PbMessageRemediationParameterTypeEnum;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLength;
import org.ietf.nea.pb.validate.rules.RpMessageTypeLimits;
import org.ietf.nea.pb.validate.rules.RpVendorIdLimits;

import de.hsbremen.tc.tnc.IETFConstants;

public class PbMessageValueRemediationParametersBuilderIetf implements PbMessageValueRemediationParametersBuilder{

    private static final byte RESERVED = 0;           //  8 bit(s) should be 0
    
    private long rpVendorId;         // 24 bit(s)
    private long rpType;             // 32 bit(s)
    
    private long length; 
    
    private AbstractPbMessageSubValue parameter;
    
    public PbMessageValueRemediationParametersBuilderIetf(){
    	this.rpVendorId = IETFConstants.IETF_PEN_VENDORID;
    	this.rpType = PbMessageRemediationParameterTypeEnum.IETF_STRING.type();
    	this.length = PbMessageTlvFixedLength.REM_PAR_VALUE.length();
    	this.parameter = null;
    }

	/* (non-Javadoc)
	 * @see org.ietf.nea.pb.message.PbMessageRemediationParametersBuilder#setRpVendorId(long)
	 */
	@Override
	public PbMessageValueRemediationParametersBuilder setRpVendorId(long rpVendorId) throws RuleException {
		
		RpVendorIdLimits.check(rpVendorId);
		this.rpVendorId = rpVendorId;
	
		return this;
	}

	/* (non-Javadoc)
	 * @see org.ietf.nea.pb.message.PbMessageRemediationParametersBuilder#setRpType(long)
	 */
	@Override
	public PbMessageValueRemediationParametersBuilder setRpType(long rpType) throws RuleException {
		
		RpMessageTypeLimits.check(rpType);
		this.rpType = rpType;
		
		return this;
	}

	/* (non-Javadoc)
	 * @see org.ietf.nea.pb.message.PbMessageRemediationParametersBuilder#setParameter(org.ietf.nea.pb.message.AbstractPbMessageValueRemediationParametersValue)
	 */
	@Override
	public PbMessageValueRemediationParametersBuilder setParameter(
			AbstractPbMessageSubValue parameter) {
		
		if(parameter != null){
			this.parameter = parameter;
			this.length = PbMessageTlvFixedLength.REM_PAR_VALUE.length() + parameter.getLength();
		}
		
		return this;
	}

	@Override
	public PbMessageValueRemediationParameters toValue() throws RuleException {
		if(parameter == null){
			throw new IllegalStateException("A message value has to be set.");
		}
		
		return new PbMessageValueRemediationParameters(RESERVED, this.rpVendorId, this.rpType, this.length, this.parameter);
	}

	@Override
	public PbMessageValueRemediationParametersBuilder clear() {

		return new PbMessageValueRemediationParametersBuilderIetf();
	}

}
