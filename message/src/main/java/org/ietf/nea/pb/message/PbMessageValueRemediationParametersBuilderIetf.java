package org.ietf.nea.pb.message;

import org.ietf.nea.pb.message.enums.PbMessageRemediationParameterTypeEnum;
import org.ietf.nea.pb.validate.rules.RpMessageTypeLimits;
import org.ietf.nea.pb.validate.rules.RpVendorIdLimits;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageValue;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageValueBuilder;

public class PbMessageValueRemediationParametersBuilderIetf implements PbMessageValueRemediationParametersBuilder{

    private static final byte RESERVED = 0;           //  8 bit(s) should be 0
    
    private long rpVendorId;         // 24 bit(s)
    private long rpType;             // 32 bit(s)
    
    private AbstractPbMessageValueRemediationParametersValue parameter;
    
    public PbMessageValueRemediationParametersBuilderIetf(){
    	this.rpVendorId = IETFConstants.IETF_PEN_VENDORID;
    	this.rpType = PbMessageRemediationParameterTypeEnum.IETF_STRING.type();
    	this.parameter = null;
    }

	/* (non-Javadoc)
	 * @see org.ietf.nea.pb.message.PbMessageRemediationParametersBuilder#setRpVendorId(long)
	 */
	@Override
	public void setRpVendorId(long rpVendorId) throws ValidationException {
		
		RpVendorIdLimits.check(rpVendorId);
		this.rpVendorId = rpVendorId;
	
	}

	/* (non-Javadoc)
	 * @see org.ietf.nea.pb.message.PbMessageRemediationParametersBuilder#setRpType(long)
	 */
	@Override
	public void setRpType(long rpType) throws ValidationException {
		
		RpMessageTypeLimits.check(rpType);
		this.rpType = rpType;
	}

	/* (non-Javadoc)
	 * @see org.ietf.nea.pb.message.PbMessageRemediationParametersBuilder#setParameter(org.ietf.nea.pb.message.AbstractPbMessageValueRemediationParametersValue)
	 */
	@Override
	public void setParameter(
			AbstractPbMessageValueRemediationParametersValue parameter) {
		
		if(parameter != null){
			this.parameter = parameter;
		}
		
	}

	@Override
	public TnccsMessageValue toValue() throws ValidationException {
		if(parameter == null){
			throw new IllegalStateException("A message value has to be set.");
		}
		
		return new PbMessageValueRemediationParameters(RESERVED, rpVendorId, rpType, parameter);
	}

	@Override
	public TnccsMessageValueBuilder clear() {

		return new PbMessageValueRemediationParametersBuilderIetf();
	}

}
