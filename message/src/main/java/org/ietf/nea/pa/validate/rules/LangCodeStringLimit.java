package org.ietf.nea.pa.validate.rules;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pa.attribute.enums.PaAttributeErrorCodeEnum;
import org.ietf.nea.pa.validate.enums.PaErrorCauseEnum;

import de.hsbremen.tc.tnc.IETFConstants;

public class LangCodeStringLimit {
	public static void check(final String value) throws RuleException{
    	if(value != null && value.length() > IETFConstants.IETF_MAX_LANG_CODE_LENGTH){
            throw new RuleException("Message language code is to large.",true,PaAttributeErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PaErrorCauseEnum.VALUE_TO_LARGE.number(),value);
        }
    }
}
