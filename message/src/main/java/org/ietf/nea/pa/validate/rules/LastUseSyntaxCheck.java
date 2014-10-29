package org.ietf.nea.pa.validate.rules;

import java.util.regex.Pattern;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pa.attribute.enums.PaAttributeErrorCodeEnum;
import org.ietf.nea.pa.validate.enums.PaErrorCauseEnum;

public class LastUseSyntaxCheck {
	public static void check(final String value) throws RuleException{
    	if(value != null){
    		if(!Pattern.matches("\\d{4}\\-\\d{2}\\-\\d{2}T\\d{2}:\\d{2}:\\d{2}Z", value)){
    			throw new RuleException("Attribute contains an unsupported time format: " + value + ".",false,PaAttributeErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PaErrorCauseEnum.TIME_FORMAT_NOT_VALID.number(),value);        
    		}
    	}
    }
}
