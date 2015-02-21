package org.ietf.nea.pb.validate.rules;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.validate.enums.PbErrorCauseEnum;

import de.hsbremen.tc.tnc.util.NotNull;

public class NoNullTerminatedString {
	 public static void check(final String value) throws RuleException{
	        NotNull.check("Value cannot be null.", value);
		 	if(value.contains("\0")){
	            throw new RuleException("Message contains Null termination sequences.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PbErrorCauseEnum.null_TERMINATION.number(),value);
	        }
	    } 
}
