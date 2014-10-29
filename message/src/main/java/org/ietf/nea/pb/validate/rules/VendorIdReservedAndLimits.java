package org.ietf.nea.pb.validate.rules;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.validate.enums.PbErrorCauseEnum;

import de.hsbremen.tc.tnc.IETFConstants;

public class VendorIdReservedAndLimits {

	public static void check(final long vendorId) throws RuleException{
		if(vendorId == IETFConstants.IETF_VENDOR_ID_RESERVED){
        	 throw new RuleException("Vendor ID is set to reserved value.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PbErrorCauseEnum.VENDOR_ID_RESERVED.number(),Long.toString(vendorId));
        		
        }
        if(vendorId > IETFConstants.IETF_MAX_VENDOR_ID){
            throw new RuleException("Vendor ID is to large.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PbErrorCauseEnum.VALUE_TO_LARGE.number(),Long.toString(vendorId));
        }
        
        if(vendorId < 0){
            throw new RuleException("Vendor ID cannot be negativ.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PbErrorCauseEnum.NEGATIV_UNSIGNED.number(),Long.toString(vendorId));
        }
    }
	
}
