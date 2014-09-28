package org.ietf.nea.pb.validate.rules;

import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.validate.enums.PbErrorCauseEnum;
import org.trustedcomputinggroup.tnc.ifimc.TNCConstants;

import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;

public class ImIdLimits {
	public static void check(final long imId) throws ValidationException{
        
        if(imId > TNCConstants.TNC_IMCID_ANY){
        	throw new ValidationException("IM(C/V) ID is to large.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PbErrorCauseEnum.VALUE_TO_LARGE.number(),Long.toString(imId));
        }
        if(imId < 0){
            throw new ValidationException("IM(C/V) ID cannot be negativ.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PbErrorCauseEnum.NEGATIV_UNSIGNED.number(),Long.toString(imId));
        }
	        
	    
    }
}
