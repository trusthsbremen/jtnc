package org.ietf.nea.pa.validate.rules;

import java.util.Arrays;
import java.util.Set;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pa.attribute.PaAttributeValue;
import org.ietf.nea.pa.attribute.enums.PaAttributeErrorCodeEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeFlagsEnum;
import org.ietf.nea.pa.validate.enums.PaErrorCauseEnum;

public class PaAttributeNoSkip {

	public static void check(PaAttributeValue value, final Set<PaAttributeFlagsEnum> flags ) throws RuleException{
		if(flags == null){
        	throw new NullPointerException("Flags can not be null.");
        }
		
		if(value == null){
			throw new NullPointerException("Value can not be null.");
		}
		
		if(!value.isOmittable()){
			 if(flags.isEmpty() || !flags.contains(PaAttributeFlagsEnum.NOSKIP)){	
		            throw new RuleException("NOSKIP must be set for this message.",true,PaAttributeErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PaErrorCauseEnum.NOSKIP_MISSING.number(),Arrays.toString(flags.toArray()));
		     }
		}else{
			if(!flags.isEmpty() && flags.contains(PaAttributeFlagsEnum.NOSKIP)){
		        throw new RuleException("NOSKIP not allowed in this message.",true,PaAttributeErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PaErrorCauseEnum.NOSKIP_NOT_ALLOWED.number(),Arrays.toString(flags.toArray()));
		    }
		}
	}
}
