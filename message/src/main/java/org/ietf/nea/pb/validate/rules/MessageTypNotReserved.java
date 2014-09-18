package org.ietf.nea.pb.validate.rules;

import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.validate.enums.PbErrorCauseEnum;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;

public class MessageTypNotReserved {

	 public static void check(final long messageType) throws ValidationException {
	        if(messageType == IETFConstants.IETF_MESSAGE_TYPE_RESERVED){
	            throw new ValidationException("Message type is set to reserved value.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PbErrorCauseEnum.MESSAGE_TYPE_RESERVED.number(),Long.toString(messageType));
	        }  
	        
	 }
	
}
