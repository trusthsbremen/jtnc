package org.ietf.nea.pb.validate.rules;

import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.validate.enums.PbErrorCauseEnum;
import org.trustedcomputinggroup.tnc.TNCConstants;

import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;

public class ImMessageTypeNotReserved {
	public static void check(final long messageType) throws ValidationException{
		
        if(messageType == TNCConstants.TNC_SUBTYPE_ANY){
            throw new ValidationException("Sub Message type is set to reserved value.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PbErrorCauseEnum.SUB_TYPE_RESERVED.number(),Long.toString(messageType));
        }  
	        
	    
    }
}
