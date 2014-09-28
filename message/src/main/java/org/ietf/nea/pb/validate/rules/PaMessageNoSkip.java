package org.ietf.nea.pb.validate.rules;

import java.util.Arrays;
import java.util.Set;

import org.ietf.nea.pb.message.AbstractPbMessageValue;
import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.message.enums.PbMessageFlagsEnum;
import org.ietf.nea.pb.message.enums.PbMessageTypeEnum;
import org.ietf.nea.pb.validate.enums.PbErrorCauseEnum;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;

public class PaMessageNoSkip {

	public static void check(AbstractPbMessageValue value, final Set<PbMessageFlagsEnum> flags ) throws ValidationException{
		if(flags == null){
        	throw new NullPointerException("Flags can not be null.");
        }
		
		if(value == null){
			throw new NullPointerException("Value can not be null.");
		}
		
		if(value.hasNoSkip()){
			 if(!flags.contains(PbMessageFlagsEnum.NOSKIP)){	
		            throw new ValidationException("NOSKIP must be set for this message.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PbErrorCauseEnum.NOSKIP_MISSING.number(),Arrays.toString(flags.toArray()));
		     }
		}else{
			if(flags.contains(PbMessageFlagsEnum.NOSKIP)){
		        throw new ValidationException("NOSKIP not allowed in this message.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PbErrorCauseEnum.NOSKIP_NOT_ALLOWED.number(),Arrays.toString(flags.toArray()));
		    }
		}
	}
}
