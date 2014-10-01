package org.ietf.nea.pb.validate.rules;

import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.validate.enums.PbErrorCauseEnum;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;

public class VendorIdReservedAndLimits {

	public static void check(final long vendorId) throws ValidationException{
		if(vendorId == IETFConstants.IETF_VENDOR_ID_RESERVED){
        	 throw new ValidationException("Vendor ID is set to reserved value.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PbErrorCauseEnum.MESSAGE_TYPE_RESERVED.number(),Long.toString(vendorId));
        		
        }
        if(vendorId > IETFConstants.IETF_MAX_VENDOR_ID){
            throw new ValidationException("Remediation parameter vendor ID is to large.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PbErrorCauseEnum.VALUE_TO_LARGE.number(),Long.toString(vendorId));
        }
        
        if(vendorId < 0){
            throw new ValidationException("Remediation parameter vendor ID cannot be negativ.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PbErrorCauseEnum.NEGATIV_UNSIGNED.number(),Long.toString(vendorId));
        }
    }
	
}
