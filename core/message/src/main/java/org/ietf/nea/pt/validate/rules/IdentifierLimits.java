package org.ietf.nea.pt.validate.rules;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pt.validate.enums.PtTlsErrorCauseEnum;
import org.ietf.nea.pt.value.enums.PtTlsMessageErrorCodeEnum;

public class IdentifierLimits {

	 public static void check(final long identifier) throws RuleException {
	      
	        if(identifier > 0xFFFFFFFFL){
	        	throw new RuleException("Identifier is to large.",true,PtTlsMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PtTlsErrorCauseEnum.VALUE_TO_LARGE.number(),identifier);
	        }
	        if(identifier < 0){
	            throw new RuleException("Identifier cannot be negativ.",true,PtTlsMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PtTlsErrorCauseEnum.NEGATIV_UNSIGNED.number(),identifier);
	        }
	 }
}
