package org.ietf.nea.pb.validate.rules;

import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;

import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;

public class NoZeroString {
	public static void check(final String value) throws ValidationException{
    	if(value == null || value == ""){
            throw new ValidationException("Message contains zero-string.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),value);
        }
    }
}
