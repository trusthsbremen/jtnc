package org.ietf.nea.pt.validate.rules;

import org.ietf.nea.pt.validate.enums.PtTlsErrorCauseEnum;
import org.ietf.nea.pt.value.enums.PtTlsMessageErrorCodeEnum;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.message.exception.RuleException;

public class VendorIdReservedAndLimits {

	public static void check(final long vendorId) throws RuleException{
		if(vendorId == IETFConstants.IETF_VENDOR_ID_RESERVED){
        	 throw new RuleException("Vendor ID is set to reserved value.",true,PtTlsMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PtTlsErrorCauseEnum.VENDOR_ID_RESERVED.number(),vendorId);
        		
        }
        if(vendorId > IETFConstants.IETF_MAX_VENDOR_ID){
            throw new RuleException("Vendor ID is to large.",true,PtTlsMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PtTlsErrorCauseEnum.VALUE_TO_LARGE.number(),vendorId);
        }
        
        if(vendorId < 0){
            throw new RuleException("Vendor ID cannot be negativ.",true,PtTlsMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PtTlsErrorCauseEnum.NEGATIV_UNSIGNED.number(),vendorId);
        }
    }
	
}
