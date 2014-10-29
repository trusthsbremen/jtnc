package org.ietf.nea.pa.validate.rules;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pa.attribute.enums.PaAttributeErrorCodeEnum;
import org.ietf.nea.pa.validate.enums.PaErrorCauseEnum;

import de.hsbremen.tc.tnc.IETFConstants;

public class TypeReservedAndLimits {

	 public static void check(final long attributeType) throws RuleException {
	        if(attributeType == IETFConstants.IETF_TYPE_RESERVED){
	            throw new RuleException("Type is set to reserved value.",false,PaAttributeErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PaErrorCauseEnum.TYPE_RESERVED.number(),Long.toString(attributeType));
	        }  
	        if(attributeType > IETFConstants.IETF_MAX_TYPE){
	        	throw new RuleException("Type is to large.",false,PaAttributeErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PaErrorCauseEnum.VALUE_TO_LARGE.number(),Long.toString(attributeType));
	        }
	        if(attributeType < 0){
	            throw new RuleException("Type cannot be negativ.",false,PaAttributeErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PaErrorCauseEnum.NEGATIV_UNSIGNED.number(),Long.toString(attributeType));
	        }
	 }
}
