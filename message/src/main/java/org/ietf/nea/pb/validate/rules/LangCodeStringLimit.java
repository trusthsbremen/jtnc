package org.ietf.nea.pb.validate.rules;

import org.ietf.nea.pb.exception.RuleException;
import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.validate.enums.PbErrorCauseEnum;

import de.hsbremen.tc.tnc.IETFConstants;

public class LangCodeStringLimit {
	public static void check(final String value) throws RuleException{
    	if(value != null && value.length() > IETFConstants.IETF_MAX_LANG_CODE_LENGTH){
            throw new RuleException("Message language code is to large.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PbErrorCauseEnum.VALUE_TO_LARGE.number(),value);
        }
    }
}
