package org.ietf.nea.pa.validate.rules;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pa.attribute.enums.PaAttributeErrorCodeEnum;
import org.ietf.nea.pa.validate.enums.PaErrorCauseEnum;

import de.hsbremen.tc.tnc.IETFConstants;

public class CommonLengthLimits {

	 public static void check(final long length) throws RuleException {

	        if(length > IETFConstants.IETF_MAX_LENGTH){
	        	throw new RuleException("Length is to large.",false,PaAttributeErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PaErrorCauseEnum.VALUE_TO_LARGE.number(),length);
	        }
	        if(length < 0){
	            throw new RuleException("Length cannot be negativ.",false,PaAttributeErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PaErrorCauseEnum.NEGATIV_UNSIGNED.number(),length);
	        }
	 }
}
