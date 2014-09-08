package org.ietf.nea.pb.validate.rules;

import java.util.Arrays;
import java.util.Set;

import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.message.enums.PbMessageFlagsEnum;

import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;

public class NoSkip {
	 public static void check(final Set<PbMessageFlagsEnum> flags) throws ValidationException {
	        if(!flags.contains(PbMessageFlagsEnum.NOSKIP)){
	        	
	            throw new ValidationException("NOSKIP must be set for this message.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),Arrays.toString(flags.toArray()));
	        }
	    }
}
