package org.ietf.nea.pb.validate.rules;

import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.validate.enums.PbErrorCauseEnum;
import org.trustedcomputinggroup.tnc.ifimc.TNCConstants;

import de.hsbremen.tc.tnc.message.exception.RuleException;

public class ImIdLimits {
	public static void check(final long imId) throws RuleException{
        
        if(imId > TNCConstants.TNC_IMCID_ANY){
        	throw new RuleException("IM(C/V) ID is to large.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PbErrorCauseEnum.VALUE_TO_LARGE.number(),imId);
        }
        if(imId < 0){
            throw new RuleException("IM(C/V) ID cannot be negativ.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PbErrorCauseEnum.NEGATIV_UNSIGNED.number(),imId);
        }
	        
	    
    }
}