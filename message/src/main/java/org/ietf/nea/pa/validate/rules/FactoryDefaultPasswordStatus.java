package org.ietf.nea.pa.validate.rules;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pa.attribute.enums.PaAttributeErrorCodeEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeFactoryDefaultPasswordStatusEnum;
import org.ietf.nea.pa.validate.enums.PaErrorCauseEnum;

public class FactoryDefaultPasswordStatus {

	public static void check(final long status) throws RuleException{
		if(PaAttributeFactoryDefaultPasswordStatusEnum.fromNumber(status) == null){
        	throw new RuleException("The type value " + status + " is unknown.",false,PaAttributeErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PaErrorCauseEnum.FACTORY_DEFAULT_PW_STATUS_NOT_SUPPORTED.number(),Long.toString(status));
        }
    }
	
}
