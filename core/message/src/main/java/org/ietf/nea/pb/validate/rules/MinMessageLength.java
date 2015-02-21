package org.ietf.nea.pb.validate.rules;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.validate.enums.PbErrorCauseEnum;

public class MinMessageLength {

	 public static void check(final long actualLength, final long minLength) throws RuleException {
		 	if(actualLength < minLength){
	            throw new RuleException("Batch/Message has an invalid length for its type.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PbErrorCauseEnum.LENGTH_TO_SHORT.number(),actualLength);
	        }
	 }
	
}
