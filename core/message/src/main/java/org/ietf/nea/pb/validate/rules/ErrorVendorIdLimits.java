package org.ietf.nea.pb.validate.rules;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.validate.enums.PbErrorCauseEnum;

import de.hsbremen.tc.tnc.IETFConstants;

public class ErrorVendorIdLimits {

	public static void check(final long vendorId) throws RuleException{
		
	        if(vendorId > IETFConstants.IETF_MAX_VENDOR_ID){
	            throw new RuleException("Error vendor ID is to large.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PbErrorCauseEnum.VALUE_TO_LARGE.number(),vendorId);
	        }
	        
	        if(vendorId < 0){
	            throw new RuleException("Error vendor ID cannot be negativ.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PbErrorCauseEnum.NEGATIV_UNSIGNED.number(),vendorId);
	        }
	        
	    }
	
}
