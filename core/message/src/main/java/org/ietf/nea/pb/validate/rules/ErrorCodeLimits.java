package org.ietf.nea.pb.validate.rules;

import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.validate.enums.PbErrorCauseEnum;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.message.exception.RuleException;

public class ErrorCodeLimits {
	public static void check(final int errorCode) throws RuleException{
        
        if((errorCode) > IETFConstants.IETF_MAX_ERROR_CODE){
        	throw new RuleException("Error code is to large.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PbErrorCauseEnum.VALUE_TO_LARGE.number(),errorCode);
        }
        if((errorCode) < 0){
            throw new RuleException("Error code cannot be negativ.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PbErrorCauseEnum.NEGATIV_UNSIGNED.number(),errorCode);
        }
	        
	    
    }
}
