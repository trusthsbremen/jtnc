package org.ietf.nea.pb.validate.rules;

import java.util.Set;

import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.message.enums.PbMessageFlagEnum;
import org.ietf.nea.pb.validate.enums.PbErrorCauseEnum;

import de.hsbremen.tc.tnc.message.exception.RuleException;

public class NoSkipOnUnknownMessage {

	public static void check(Set<PbMessageFlagEnum> flags) throws RuleException{
		if(flags.contains(PbMessageFlagEnum.NOSKIP)){
			throw new RuleException("Message is not supported but has No Skip set.",true,PbMessageErrorCodeEnum.IETF_UNSUPPORTED_MANDATORY_MESSAGE.code(),PbErrorCauseEnum.NOT_SPECIFIED.number());
		}
	}
	
}
