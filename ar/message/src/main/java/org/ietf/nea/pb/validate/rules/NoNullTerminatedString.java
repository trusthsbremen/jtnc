package org.ietf.nea.pb.validate.rules;

import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;

import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;

public class NoNullTerminatedString {
	 public static void check(final String value) throws ValidationException{
	        if(value == null){
	        	throw new NullPointerException("Value cannot be null.");
	        }
		 	if(value.contains("\0")){
	            throw new ValidationException("Message contains Null termination sequences.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),value);
	        }
	    } 
}
