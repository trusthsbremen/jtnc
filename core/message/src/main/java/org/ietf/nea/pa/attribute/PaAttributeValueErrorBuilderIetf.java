package org.ietf.nea.pa.attribute;

import org.ietf.nea.pa.attribute.enums.PaAttributeErrorCodeEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;
import org.ietf.nea.pa.attribute.util.AbstractPaAttributeValueErrorInformation;
import org.ietf.nea.pa.validate.rules.TypeReservedAndLimits;
import org.ietf.nea.pa.validate.rules.VendorIdReservedAndLimits;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.message.exception.RuleException;

public class PaAttributeValueErrorBuilderIetf implements PaAttributeValueErrorBuilder{
    
    private long errorVendorId;         // 24 bit(s)
    private long errorCode;             // 32 bit(s)
    
    private long length; 
    
    private AbstractPaAttributeValueErrorInformation errorInformation;
    
    public PaAttributeValueErrorBuilderIetf(){
    	this.errorVendorId = IETFConstants.IETF_PEN_VENDORID;
    	this.errorCode = PaAttributeErrorCodeEnum.IETF_INVALID_PARAMETER.code();
    	this.length = PaAttributeTlvFixedLengthEnum.ERR_INF.length();
    	this.errorInformation = null;
    }

	@Override
	public PaAttributeValueErrorBuilder setErrorVendorId(long rpVendorId) throws RuleException {
		
		VendorIdReservedAndLimits.check(rpVendorId);
		this.errorVendorId = rpVendorId;
	
		return this;
	}

	@Override
	public PaAttributeValueErrorBuilder setErrorCode(long rpType) throws RuleException {
		
		TypeReservedAndLimits.check(rpType);
		this.errorCode = rpType;
		
		return this;
	}

	@Override
	public PaAttributeValueErrorBuilder setErrorInformation(
			AbstractPaAttributeValueErrorInformation parameter) {
		
		if(parameter != null){
			this.errorInformation = parameter;
			this.length = PaAttributeTlvFixedLengthEnum.ERR_INF.length() + parameter.getLength();
		}
		
		return this;
	}

	@Override
	public PaAttributeValueError toObject(){
		if(errorInformation == null){
			throw new IllegalStateException("An error information has to be set.");
		}
		
		return new PaAttributeValueError(this.length, this.errorVendorId, this.errorCode, this.errorInformation);
	}

	@Override
	public PaAttributeValueErrorBuilder newInstance() {

		return new PaAttributeValueErrorBuilderIetf();
	}

}
