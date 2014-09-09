package org.ietf.nea.pb.validate.rules;

import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;

import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;

public class BatchVersion {

	public static void check(final byte actuallVersion, final byte supportedVersion) throws ValidationException{
        if(actuallVersion != supportedVersion){
            throw new ValidationException("The version "+actuallVersion+" is not supported.",true,PbMessageErrorCodeEnum.IETF_UNSUPPORTED_VERSION.code(),Long.toString(actuallVersion));
        }
    }
	
}
