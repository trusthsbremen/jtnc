package org.ietf.nea.pa.validate.rules;

import java.util.Set;

import org.ietf.nea.pa.attribute.enums.PaAttributeErrorCodeEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeFlagsEnum;
import org.ietf.nea.pb.validate.enums.PbErrorCauseEnum;

import de.hsbremen.tc.tnc.message.exception.RuleException;

public class NoSkipOnUnknownAttribute {

	public static void check(Set<PaAttributeFlagsEnum> flags) throws RuleException{
		if(flags.contains(PaAttributeFlagsEnum.NOSKIP)){
			throw new RuleException("Attribute is not supported but has No Skip set.",true,PaAttributeErrorCodeEnum.IETF_UNSUPPORTED_MANDATORY_ATTRIBUTE.code(),PbErrorCauseEnum.NOT_SPECIFIED.number());
		}
	}
	
}
