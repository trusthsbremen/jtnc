package org.ietf.nea.pb.validate.rules;

import java.util.Set;

import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.message.enums.PbMessageFlagsEnum;
import org.ietf.nea.pb.validate.enums.PbErrorCauseEnum;

import de.hsbremen.tc.tnc.message.exception.RuleException;

public class NoSkipOnUnknownMessage {

	public static void check(Set<PbMessageFlagsEnum> flags) throws RuleException{
		if(flags.contains(PbMessageFlagsEnum.NOSKIP)){
			throw new RuleException("Message is not supported but has No Skip set.",true,PbMessageErrorCodeEnum.IETF_UNSUPPORTED_MANDATORY_MESSAGE.code(),PbErrorCauseEnum.NOT_SPECIFIED.number());
		}
	}
	
}
