package org.ietf.nea.pt.validate.rules;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pt.validate.enums.PtTlsErrorCauseEnum;
import org.ietf.nea.pt.value.enums.PtTlsMessageErrorCodeEnum;

import de.hsbremen.tc.tnc.HSBConstants;

public class ConfiguredLengthLimit {

	 public static void check(final long length, final long maxlength) throws RuleException {
		 
		 if(maxlength != HSBConstants.HSB_TRSPT_MAX_MESSAGE_SIZE_UNKNOWN && maxlength != HSBConstants.HSB_TRSPT_MAX_MESSAGE_SIZE_UNLIMITED){
			 if(length > maxlength){
				 throw new RuleException("Length is to large. Configured limit is " +maxlength+ ".",true,PtTlsMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PtTlsErrorCauseEnum.VALUE_TO_LARGE.number(),length, maxlength);
 
			 }
		 }
	 }
}
