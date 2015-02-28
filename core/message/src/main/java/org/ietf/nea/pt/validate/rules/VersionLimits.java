package org.ietf.nea.pt.validate.rules;

import org.ietf.nea.pt.validate.enums.PtTlsErrorCauseEnum;
import org.ietf.nea.pt.value.enums.PtTlsMessageErrorCodeEnum;

import de.hsbremen.tc.tnc.message.exception.RuleException;

public class VersionLimits {

	 public static void check(final short version) throws RuleException {
	     
	        if(version > 0xFF){
	        	throw new RuleException("Version is to large.",true,PtTlsMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PtTlsErrorCauseEnum.VALUE_TO_LARGE.number(),version);
	        }
	        if(version < 0){
	            throw new RuleException("Version cannot be negativ.",true,PtTlsMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PtTlsErrorCauseEnum.NEGATIV_UNSIGNED.number(),version);
	        }
	 }
}
