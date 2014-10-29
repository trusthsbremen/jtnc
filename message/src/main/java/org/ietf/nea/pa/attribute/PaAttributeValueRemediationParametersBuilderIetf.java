package org.ietf.nea.pa.attribute;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pa.attribute.enums.PaAttributeRemediationParameterTypeEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLength;
import org.ietf.nea.pa.attribute.util.AbstractPaAttributeValueRemediationParameter;
import org.ietf.nea.pa.validate.rules.TypeReservedAndLimits;
import org.ietf.nea.pa.validate.rules.VendorIdReservedAndLimits;

import de.hsbremen.tc.tnc.IETFConstants;

public class PaAttributeValueRemediationParametersBuilderIetf implements PaAttributeValueRemediationParametersBuilder{
    
    private long rpVendorId;         // 24 bit(s)
    private long rpType;             // 32 bit(s)
    
    private long length; 
    
    private AbstractPaAttributeValueRemediationParameter parameter;
    
    public PaAttributeValueRemediationParametersBuilderIetf(){
    	this.rpVendorId = IETFConstants.IETF_PEN_VENDORID;
    	this.rpType = PaAttributeRemediationParameterTypeEnum.IETF_STRING.type();
    	this.length = PaAttributeTlvFixedLength.REM_PAR.length();
    	this.parameter = null;
    }

	/* (non-Javadoc)
	 * @see org.ietf.nea.pb.message.PbMessageRemediationParametersBuilder#setRpVendorId(long)
	 */
	@Override
	public PaAttributeValueRemediationParametersBuilder setRpVendorId(long rpVendorId) throws RuleException {
		
		VendorIdReservedAndLimits.check(rpVendorId);
		this.rpVendorId = rpVendorId;
	
		return this;
	}

	/* (non-Javadoc)
	 * @see org.ietf.nea.pb.message.PbMessageRemediationParametersBuilder#setRpType(long)
	 */
	@Override
	public PaAttributeValueRemediationParametersBuilder setRpType(long rpType) throws RuleException {
		
		TypeReservedAndLimits.check(rpType);
		this.rpType = rpType;
		
		return this;
	}

	/* (non-Javadoc)
	 * @see org.ietf.nea.pb.message.PbMessageRemediationParametersBuilder#setParameter(org.ietf.nea.pb.message.AbstractPbMessageValueRemediationParametersValue)
	 */
	@Override
	public PaAttributeValueRemediationParametersBuilder setParameter(
			AbstractPaAttributeValueRemediationParameter parameter) {
		
		if(parameter != null){
			this.parameter = parameter;
			this.length = PaAttributeTlvFixedLength.REM_PAR.length() + parameter.getLength();
		}
		
		return this;
	}

	@Override
	public PaAttributeValueRemediationParameters toValue(){
		if(parameter == null){
			throw new IllegalStateException("A message value has to be set.");
		}
		
		return new PaAttributeValueRemediationParameters(this.rpVendorId, this.rpType, this.length, this.parameter);
	}

	@Override
	public PaAttributeValueRemediationParametersBuilder clear() {

		return new PaAttributeValueRemediationParametersBuilderIetf();
	}

}
