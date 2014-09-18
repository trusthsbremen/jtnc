package org.ietf.nea.pb.validate.rules;

import java.util.Arrays;
import java.util.Set;

import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.message.enums.PbMessageFlagsEnum;
import org.ietf.nea.pb.validate.enums.PbErrorCauseEnum;

import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;

public class NoNoSkip {

	public static void check(final Set<PbMessageFlagsEnum> flags) throws ValidationException {
        if(flags == null){
        	throw new NullPointerException("Message  argument can not be null.");
        }
		if(flags.contains(PbMessageFlagsEnum.NOSKIP)){
            throw new ValidationException("NOSKIP not allowed in this message.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PbErrorCauseEnum.NOSKIP_NOT_ALLOWED.number(),Arrays.toString(flags.toArray()));
        }
    }
}
