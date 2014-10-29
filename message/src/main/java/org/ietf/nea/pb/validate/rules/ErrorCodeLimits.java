package org.ietf.nea.pb.validate.rules;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.validate.enums.PbErrorCauseEnum;

import de.hsbremen.tc.tnc.IETFConstants;

public class ErrorCodeLimits {
	public static void check(final short errorCode) throws RuleException{
        
        if((errorCode & 0xFFFF) > IETFConstants.IETF_MAX_ERROR_CODE){
        	throw new RuleException("Error code is to large.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PbErrorCauseEnum.VALUE_TO_LARGE.number(),Integer.toString(errorCode & 0xFFFF));
        }
        if((errorCode & 0xFFFF) < 0){
            throw new RuleException("Error code cannot be negativ.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PbErrorCauseEnum.NEGATIV_UNSIGNED.number(),Integer.toString(errorCode & 0xFFFF));
        }
	        
	    
    }
}
