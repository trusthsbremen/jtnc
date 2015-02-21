package org.ietf.nea.pa.validate.rules;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pa.attribute.enums.PaAttributeErrorCodeEnum;
import org.ietf.nea.pa.validate.enums.PaErrorCauseEnum;

public class StringLengthLimit {
	public static void check(final String value, final int maxLength) throws RuleException{
    	if(value != null && value.length() > maxLength){
            throw new RuleException("String is to large.",true,PaAttributeErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PaErrorCauseEnum.VALUE_TO_LARGE.number(),value);
        }
    }
}
