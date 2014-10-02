package org.ietf.nea.pb.validate.rules;

import org.ietf.nea.pb.exception.RuleException;
import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.validate.enums.PbErrorCauseEnum;

public class NoZeroString {
	public static void check(final String value) throws RuleException{
    	if(value == null || value == ""){
            throw new RuleException("Message contains zero-string.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PbErrorCauseEnum.ZERO_STRING.number(),value);
        }
    }
}
