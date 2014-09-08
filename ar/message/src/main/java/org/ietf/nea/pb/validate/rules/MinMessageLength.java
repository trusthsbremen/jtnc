package org.ietf.nea.pb.validate.rules;

import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;

import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;

public class MinMessageLength {

	 public static void check(final long actualLength, final long minLength) throws ValidationException {
		 	if(actualLength < minLength){
	            throw new ValidationException("Message has an invalid length for its type.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),Long.toString(actualLength));
	        }
	 }
	
}
