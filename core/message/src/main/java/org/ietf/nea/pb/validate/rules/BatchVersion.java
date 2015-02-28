package org.ietf.nea.pb.validate.rules;

import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.validate.enums.PbErrorCauseEnum;

import de.hsbremen.tc.tnc.message.exception.RuleException;

public class BatchVersion {

	public static void check(final short actualVersion, final short supportedVersion) throws RuleException{
        if(actualVersion != supportedVersion){
        	// the version parameter are added as reasons first actual, than supported. This is only by convention!
            throw new RuleException("The version "+actualVersion+" is not supported.",true,PbMessageErrorCodeEnum.IETF_UNSUPPORTED_VERSION.code(),PbErrorCauseEnum.BATCH_VERSION_NOT_SUPPORTED.number(),actualVersion, supportedVersion);
        }
    }
	
}
