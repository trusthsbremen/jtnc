package org.ietf.nea.pa.validate.rules;

import org.ietf.nea.pa.attribute.enums.PaAttributeErrorCodeEnum;
import org.ietf.nea.pa.validate.enums.PaErrorCauseEnum;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.message.exception.RuleException;

public class VendorIdReservedAndLimits {

	public static void check(final long vendorId) throws RuleException{
		if(vendorId == IETFConstants.IETF_VENDOR_ID_RESERVED){
        	 throw new RuleException("Vendor ID is set to reserved value.",false,PaAttributeErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PaErrorCauseEnum.VENDOR_ID_RESERVED.number(),vendorId);
        		
        }
        if(vendorId > IETFConstants.IETF_MAX_VENDOR_ID){
            throw new RuleException("Vendor ID is to large.",false,PaAttributeErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PaErrorCauseEnum.VALUE_TO_LARGE.number(),vendorId);
        }
        
        if(vendorId < 0){
            throw new RuleException("Vendor ID cannot be negativ.",false,PaAttributeErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PaErrorCauseEnum.NEGATIV_UNSIGNED.number(),vendorId);
        }
    }
	
}
