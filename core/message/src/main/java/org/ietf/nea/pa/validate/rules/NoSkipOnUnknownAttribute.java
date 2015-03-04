package org.ietf.nea.pa.validate.rules;

import java.util.Set;

import org.ietf.nea.pa.attribute.enums.PaAttributeErrorCodeEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeFlagEnum;
import org.ietf.nea.pb.validate.enums.PbErrorCauseEnum;

import de.hsbremen.tc.tnc.message.exception.RuleException;

public class NoSkipOnUnknownAttribute {

	public static void check(Set<PaAttributeFlagEnum> flags) throws RuleException{
		if(flags.contains(PaAttributeFlagEnum.NOSKIP)){
			throw new RuleException("Attribute is not supported but has No Skip set.",true,PaAttributeErrorCodeEnum.IETF_UNSUPPORTED_MANDATORY_ATTRIBUTE.code(),PbErrorCauseEnum.NOT_SPECIFIED.number());
		}
	}
	
}
