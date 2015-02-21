package org.ietf.nea.pt.validate.rules;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pt.validate.enums.PtTlsErrorCauseEnum;
import org.ietf.nea.pt.value.enums.PtTlsMessageErrorCodeEnum;

import de.hsbremen.tc.tnc.IETFConstants;

public class CommonLengthLimits {

	 public static void check(final long length) throws RuleException {

	        if(length > IETFConstants.IETF_MAX_LENGTH){
	        	throw new RuleException("Length is to large.",true,PtTlsMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PtTlsErrorCauseEnum.VALUE_TO_LARGE.number(),length);
	        }
	        if(length < 0){
	            throw new RuleException("Length cannot be negativ.",true,PtTlsMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PtTlsErrorCauseEnum.NEGATIV_UNSIGNED.number(),length);
	        }
	 }
}
