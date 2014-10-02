package org.ietf.nea.pb.validate.rules;

import org.ietf.nea.pb.exception.RuleException;
import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.validate.enums.PbErrorCauseEnum;

public class NoNullTerminatedString {
	 public static void check(final String value) throws RuleException{
	        if(value == null){
	        	throw new NullPointerException("Value cannot be null.");
	        }
		 	if(value.contains("\0")){
	            throw new RuleException("Message contains Null termination sequences.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PbErrorCauseEnum.NULL_TERMINATION.number(),value);
	        }
	    } 
}
