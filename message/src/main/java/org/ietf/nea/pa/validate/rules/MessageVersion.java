package org.ietf.nea.pa.validate.rules;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pa.attribute.enums.PaAttributeErrorCodeEnum;
import org.ietf.nea.pa.validate.enums.PaErrorCauseEnum;

public class MessageVersion {

	public static void check(final short actuallVersion, final short supportedVersion) throws RuleException{
        if(actuallVersion != supportedVersion){
            throw new RuleException("The version "+actuallVersion+" is not supported.",true,PaAttributeErrorCodeEnum.IETF_UNSUPPORTED_VERSION.code(),PaErrorCauseEnum.MESSAGE_VERSION_NOT_SUPPORTED.number(),actuallVersion);
        }
    }
	
}
