package org.ietf.nea.pb.validate.rules;

import org.ietf.nea.pb.exception.RuleException;
import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.validate.enums.PbErrorCauseEnum;

import de.hsbremen.tc.tnc.IETFConstants;

public class MessageReservedAndLimits {

	 public static void check(final long messageType) throws RuleException {
	        if(messageType == IETFConstants.IETF_MESSAGE_TYPE_RESERVED){
	            throw new RuleException("Message type is set to reserved value.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PbErrorCauseEnum.MESSAGE_TYPE_RESERVED.number(),Long.toString(messageType));
	        }  
	        if(messageType > IETFConstants.IETF_MAX_TYPE){
	        	throw new RuleException("Message type is to large.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PbErrorCauseEnum.VALUE_TO_LARGE.number(),Long.toString(messageType));
	        }
	        if(messageType < 0){
	            throw new RuleException("Message type cannot be negativ.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PbErrorCauseEnum.NEGATIV_UNSIGNED.number(),Long.toString(messageType));
	        }
	 }
}
