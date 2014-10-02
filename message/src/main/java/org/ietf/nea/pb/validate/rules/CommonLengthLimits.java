package org.ietf.nea.pb.validate.rules;

import org.ietf.nea.pb.exception.RuleException;
import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.validate.enums.PbErrorCauseEnum;

import de.hsbremen.tc.tnc.IETFConstants;

public class CommonLengthLimits {

	 public static void check(final long messageLength) throws RuleException {

	        if(messageLength > IETFConstants.IETF_MAX_TYPE){
	        	throw new RuleException("Length is to large.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PbErrorCauseEnum.VALUE_TO_LARGE.number(),Long.toString(messageLength));
	        }
	        if(messageLength < 0){
	            throw new RuleException("Length cannot be negativ.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PbErrorCauseEnum.NEGATIV_UNSIGNED.number(),Long.toString(messageLength));
	        }
	 }
}
