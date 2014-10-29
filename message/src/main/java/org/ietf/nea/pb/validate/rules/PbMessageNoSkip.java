package org.ietf.nea.pb.validate.rules;

import java.util.Arrays;
import java.util.Set;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pb.message.PbMessageValue;
import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.message.enums.PbMessageFlagsEnum;
import org.ietf.nea.pb.validate.enums.PbErrorCauseEnum;

public class PbMessageNoSkip {

	public static void check(PbMessageValue value, final Set<PbMessageFlagsEnum> flags ) throws RuleException{
		if(flags == null){
        	throw new NullPointerException("Flags can not be null.");
        }
		
		if(value == null){
			throw new NullPointerException("Value can not be null.");
		}
		
		if(!value.isOmittable()){
			 if(flags.isEmpty() || !flags.contains(PbMessageFlagsEnum.NOSKIP)){	
		            throw new RuleException("NOSKIP must be set for this message.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PbErrorCauseEnum.NOSKIP_MISSING.number(),Arrays.toString(flags.toArray()));
		     }
		}else{
			if(!flags.isEmpty() && flags.contains(PbMessageFlagsEnum.NOSKIP)){
		        throw new RuleException("NOSKIP not allowed in this message.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PbErrorCauseEnum.NOSKIP_NOT_ALLOWED.number(),Arrays.toString(flags.toArray()));
		    }
		}
	}
}
