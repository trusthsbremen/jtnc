package org.ietf.nea.pa.validate.rules;

import java.util.Arrays;
import java.util.Set;

import org.ietf.nea.pa.attribute.PaAttributeValue;
import org.ietf.nea.pa.attribute.enums.PaAttributeErrorCodeEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeFlagEnum;
import org.ietf.nea.pa.validate.enums.PaErrorCauseEnum;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.util.NotNull;

public class PaAttributeNoSkip {

	public static void check(PaAttributeValue value, final Set<PaAttributeFlagEnum> flags ) throws RuleException{
		NotNull.check("Flags can not be null.", flags);
		
		NotNull.check("Value can not be null.", value);
		
		if(!value.isOmittable()){
			 if(flags.isEmpty() || !flags.contains(PaAttributeFlagEnum.NOSKIP)){	
		            throw new RuleException("NOSKIP must be set for this attribute.",true,PaAttributeErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PaErrorCauseEnum.NOSKIP_MISSING.number(),Arrays.toString(flags.toArray()));
		     }
		}else{
			if(!flags.isEmpty() && flags.contains(PaAttributeFlagEnum.NOSKIP)){
		        throw new RuleException("NOSKIP not allowed in this attribute.",true,PaAttributeErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PaErrorCauseEnum.NOSKIP_NOT_ALLOWED.number(),Arrays.toString(flags.toArray()));
		    }
		}
	}
}
