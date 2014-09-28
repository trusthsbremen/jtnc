package org.ietf.nea.pb.validate.rules;

import java.util.EnumSet;

import org.ietf.nea.pb.message.AbstractPbMessageValue;
import org.ietf.nea.pb.message.PbMessageValueUnknown;
import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.message.enums.PbMessageFlagsEnum;
import org.ietf.nea.pb.validate.enums.PbErrorCauseEnum;

import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;

public class PaMessageUnknownButNoSkip {

	public static void check(AbstractPbMessageValue value, EnumSet<PbMessageFlagsEnum> flags) throws ValidationException {
		if(flags.contains(PbMessageFlagsEnum.NOSKIP) && value instanceof PbMessageValueUnknown){
			throw new ValidationException("Message is not supported but has No Skip set.",true,PbMessageErrorCodeEnum.IETF_UNSUPPORTED_MANDATORY_MESSAGE.code(),PbErrorCauseEnum.NOT_SPECIFIED.number());
		    
		}
		
	}

}
