package org.ietf.nea.pb.validate.rules;

import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.validate.enums.PbErrorCauseEnum;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;

public class ErrorVendorIdLimits {

	public static void check(final long vendorId) throws ValidationException{
		
	        if(vendorId > IETFConstants.IETF_MAX_VENDOR_ID){
	            throw new ValidationException("Error vendor ID is to large.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PbErrorCauseEnum.VALUE_TO_LARGE.number(),Long.toString(vendorId));
	        }
	        
	        if(vendorId < 0){
	            throw new ValidationException("Error vendor ID cannot be negativ.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PbErrorCauseEnum.NEGATIV_UNSIGNED.number(),Long.toString(vendorId));
	        }
	        
	    }
	
}
