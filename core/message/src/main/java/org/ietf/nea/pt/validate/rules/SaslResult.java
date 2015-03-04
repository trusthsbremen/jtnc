package org.ietf.nea.pt.validate.rules;

import org.ietf.nea.pt.validate.enums.PtTlsErrorCauseEnum;
import org.ietf.nea.pt.value.enums.PtTlsMessageErrorCodeEnum;
import org.ietf.nea.pt.value.enums.PtTlsSaslResultEnum;

import de.hsbremen.tc.tnc.message.exception.RuleException;

public class SaslResult {

	public static void check(final int result) throws RuleException{
		if(PtTlsSaslResultEnum.fromId(result) == null){
        	throw new RuleException("The result value " + result + " is unknown.",true,PtTlsMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PtTlsErrorCauseEnum.SASL_RESULT_NOT_SUPPORTED.number(),result);
        }
    }
	
}
