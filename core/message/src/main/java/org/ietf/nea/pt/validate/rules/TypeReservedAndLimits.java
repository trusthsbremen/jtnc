package org.ietf.nea.pt.validate.rules;

import org.ietf.nea.pt.validate.enums.PtTlsErrorCauseEnum;
import org.ietf.nea.pt.value.enums.PtTlsMessageErrorCodeEnum;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.message.exception.RuleException;

public class TypeReservedAndLimits {

	 public static void check(final long messageType) throws RuleException {
	        if(messageType == IETFConstants.IETF_TYPE_RESERVED){
	            throw new RuleException("Message type is set to reserved value.",true,PtTlsMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PtTlsErrorCauseEnum.MESSAGE_TYPE_RESERVED.number(),messageType);
	        }  
	        if(messageType > IETFConstants.IETF_MAX_TYPE){
	        	throw new RuleException("Message type is to large.",true,PtTlsMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PtTlsErrorCauseEnum.VALUE_TO_LARGE.number(),messageType);
	        }
	        if(messageType < 0){
	            throw new RuleException("Message type cannot be negativ.",true,PtTlsMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PtTlsErrorCauseEnum.NEGATIV_UNSIGNED.number(),messageType);
	        }
	 }
}
