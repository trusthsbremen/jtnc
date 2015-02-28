package org.ietf.nea.pt.validate.rules;

import org.ietf.nea.pt.validate.enums.PtTlsErrorCauseEnum;
import org.ietf.nea.pt.value.enums.PtTlsMessageErrorCodeEnum;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.util.NotNull;

public class SaslMechanismName {
	
	public static void check(final String value) throws RuleException{
		NotNull.check("Value cannot be null.", value);
	 	if(value.contains("\0")){
	        throw new RuleException("Message contains Null termination sequences.",true,PtTlsMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PtTlsErrorCauseEnum.null_TERMINATION.number(),value);
	    }
	 	
	 	
	 	if(!value.matches("[A-Z0-9\\-_]{1,20}")){
	 		 throw new RuleException("SASL mechanism name does not match RFC 4422 conventions.",true,PtTlsMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PtTlsErrorCauseEnum.SASL_NAMING_MISMATCH.number(),value);
	 	}
	}
}
