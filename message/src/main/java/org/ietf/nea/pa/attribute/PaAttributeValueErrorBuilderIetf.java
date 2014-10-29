package org.ietf.nea.pa.attribute;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pa.attribute.enums.PaAttributeErrorCodeEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLength;
import org.ietf.nea.pa.attribute.util.AbstractPaAttributeValueErrorInformation;
import org.ietf.nea.pa.validate.rules.TypeReservedAndLimits;
import org.ietf.nea.pa.validate.rules.VendorIdReservedAndLimits;

import de.hsbremen.tc.tnc.IETFConstants;

public class PaAttributeValueErrorBuilderIetf implements PaAttributeValueErrorBuilder{
    
    private long errorVendorId;         // 24 bit(s)
    private long errorCode;             // 32 bit(s)
    
    private long length; 
    
    private AbstractPaAttributeValueErrorInformation errorInformation;
    
    public PaAttributeValueErrorBuilderIetf(){
    	this.errorVendorId = IETFConstants.IETF_PEN_VENDORID;
    	this.errorCode = PaAttributeErrorCodeEnum.IETF_INVALID_PARAMETER.code();
    	this.length = PaAttributeTlvFixedLength.ERR_INF.length();
    	this.errorInformation = null;
    }

	/* (non-Javadoc)
	 * @see org.ietf.nea.pb.message.PbMessageRemediationParametersBuilder#setRpVendorId(long)
	 */
	@Override
	public PaAttributeValueErrorBuilder setErrorVendorId(long rpVendorId) throws RuleException {
		
		VendorIdReservedAndLimits.check(rpVendorId);
		this.errorVendorId = rpVendorId;
	
		return this;
	}

	/* (non-Javadoc)
	 * @see org.ietf.nea.pb.message.PbMessageRemediationParametersBuilder#setRpType(long)
	 */
	@Override
	public PaAttributeValueErrorBuilder setErrorCode(long rpType) throws RuleException {
		
		TypeReservedAndLimits.check(rpType);
		this.errorCode = rpType;
		
		return this;
	}

	/* (non-Javadoc)
	 * @see org.ietf.nea.pb.message.PbMessageRemediationParametersBuilder#setParameter(org.ietf.nea.pb.message.AbstractPbMessageValueRemediationParametersValue)
	 */
	@Override
	public PaAttributeValueErrorBuilder setErrorInformation(
			AbstractPaAttributeValueErrorInformation parameter) {
		
		if(parameter != null){
			this.errorInformation = parameter;
			this.length = PaAttributeTlvFixedLength.ERR_INF.length() + parameter.getLength();
		}
		
		return this;
	}

	@Override
	public PaAttributeValueError toValue(){
		if(errorInformation == null){
			throw new IllegalStateException("An error information has to be set.");
		}
		
		return new PaAttributeValueError(this.length, this.errorVendorId, this.errorCode, this.errorInformation);
	}

	@Override
	public PaAttributeValueErrorBuilder clear() {

		return new PaAttributeValueErrorBuilderIetf();
	}

}
