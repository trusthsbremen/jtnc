package org.ietf.nea.pb.validate.rules;

import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.validate.enums.PbErrorCauseEnum;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.message.exception.RuleException;

public class CommonLengthLimits {

	 public static void check(final long length) throws RuleException {

	        if(length > IETFConstants.IETF_MAX_LENGTH){
	        	throw new RuleException("Length is to large.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PbErrorCauseEnum.VALUE_TO_LARGE.number(),length);
	        }
	        if(length < 0){
	            throw new RuleException("Length cannot be negativ.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PbErrorCauseEnum.NEGATIV_UNSIGNED.number(),length);
	        }
	 }
}
