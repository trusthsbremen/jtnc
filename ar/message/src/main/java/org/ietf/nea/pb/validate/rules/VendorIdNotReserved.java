package org.ietf.nea.pb.validate.rules;

import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.trustedcomputinggroup.tnc.TNCConstants;

import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;

public class VendorIdNotReserved {

	public static void check(final long vendorId) throws ValidationException{
        if(vendorId == TNCConstants.TNC_VENDORID_ANY){
            throw new ValidationException("Vendor ID is set to reserved value.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),Long.toString(vendorId));
        }
    }
	
}
