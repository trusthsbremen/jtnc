package org.ietf.nea.pb.validate.rules;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.validate.enums.PbErrorCauseEnum;

import de.hsbremen.tc.tnc.IETFConstants;

public class TypeReservedAndLimits {

	 public static void check(final long messageType) throws RuleException {
	        if(messageType == IETFConstants.IETF_TYPE_RESERVED){
	            throw new RuleException("Message type is set to reserved value.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PbErrorCauseEnum.MESSAGE_TYPE_RESERVED.number(),messageType);
	        }  
	        if(messageType > IETFConstants.IETF_MAX_TYPE){
	        	throw new RuleException("Message type is to large.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PbErrorCauseEnum.VALUE_TO_LARGE.number(),messageType);
	        }
	        if(messageType < 0){
	            throw new RuleException("Message type cannot be negativ.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PbErrorCauseEnum.NEGATIV_UNSIGNED.number(),messageType);
	        }
	 }
}
