package org.ietf.nea.pb.validate.rules;

import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.validate.enums.PbErrorCauseEnum;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;

public class ErrorCodeLimits {
	public static void check(final short errorCode) throws ValidationException{
        
        if((errorCode & 0xFFFF) > IETFConstants.IETF_MAX_ERROR_CODE){
        	throw new ValidationException("Error code is to large.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PbErrorCauseEnum.VALUE_TO_LARGE.number(),Integer.toString(errorCode & 0xFFFF));
        }
        if((errorCode & 0xFFFF) < 0){
            throw new ValidationException("Error code cannot be negativ.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PbErrorCauseEnum.NEGATIV_UNSIGNED.number(),Integer.toString(errorCode & 0xFFFF));
        }
	        
	    
    }
}
