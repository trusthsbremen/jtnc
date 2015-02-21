package org.ietf.nea.pa.validate.rules;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pa.attribute.enums.PaAttributeErrorCodeEnum;
import org.ietf.nea.pa.validate.enums.PaErrorCauseEnum;

import de.hsbremen.tc.tnc.util.NotNull;

public class NoNullTerminatedString {
	 public static void check(final String value) throws RuleException{
	        NotNull.check("Value cannot be null.", value);
		 	if(value.contains("\0")){
	            throw new RuleException("Attribute contains Null termination sequences.",false,PaAttributeErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PaErrorCauseEnum.null_TERMINATION.number(),value);
	        }
	    } 
}
