package org.ietf.nea.pb.validate.rules;

import org.ietf.nea.pb.exception.RuleException;
import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.validate.enums.PbErrorCauseEnum;

import de.hsbremen.tc.tnc.IETFConstants;

public class RpMessageTypeLimits {
	public static void check(final long messageType) throws RuleException{

        if(messageType > IETFConstants.IETF_MAX_TYPE){
        	throw new RuleException("Remediation parameter type is to large.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PbErrorCauseEnum.VALUE_TO_LARGE.number(),Long.toString(messageType));
        }
        if(messageType < 0){
            throw new RuleException("Remediation parameter type cannot be negativ.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PbErrorCauseEnum.NEGATIV_UNSIGNED.number(),Long.toString(messageType));
        }
	        
	    
    }
}
