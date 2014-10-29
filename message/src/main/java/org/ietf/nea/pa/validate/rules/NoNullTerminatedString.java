package org.ietf.nea.pa.validate.rules;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pa.attribute.enums.PaAttributeErrorCodeEnum;
import org.ietf.nea.pa.validate.enums.PaErrorCauseEnum;

public class NoNullTerminatedString {
	 public static void check(final String value) throws RuleException{
	        if(value == null){
	        	throw new NullPointerException("Value cannot be null.");
	        }
		 	if(value.contains("\0")){
	            throw new RuleException("Attribute contains Null termination sequences.",false,PaAttributeErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PaErrorCauseEnum.NULL_TERMINATION.number(),value);
	        }
	    } 
}
