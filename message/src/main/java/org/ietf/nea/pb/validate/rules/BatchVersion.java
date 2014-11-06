package org.ietf.nea.pb.validate.rules;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.validate.enums.PbErrorCauseEnum;

public class BatchVersion {

	public static void check(final byte actuallVersion, final byte supportedVersion) throws RuleException{
        if(actuallVersion != supportedVersion){
            throw new RuleException("The version "+actuallVersion+" is not supported.",true,PbMessageErrorCodeEnum.IETF_UNSUPPORTED_VERSION.code(),PbErrorCauseEnum.BATCH_VERSION_NOT_SUPPORTED.number(),actuallVersion);
        }
    }
	
}
