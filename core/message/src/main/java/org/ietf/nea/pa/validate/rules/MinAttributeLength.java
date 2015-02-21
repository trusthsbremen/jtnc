package org.ietf.nea.pa.validate.rules;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pa.attribute.enums.PaAttributeErrorCodeEnum;
import org.ietf.nea.pa.validate.enums.PaErrorCauseEnum;

public class MinAttributeLength {

	 public static void check(final long actualLength, final long minLength) throws RuleException {
		 	if(actualLength < minLength){
	            throw new RuleException("Attribute has an invalid length for its type.",false,PaAttributeErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PaErrorCauseEnum.LENGTH_TO_SHORT.number(),actualLength);
	        }
	 }
	
}
