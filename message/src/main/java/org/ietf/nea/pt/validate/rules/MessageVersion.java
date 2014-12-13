package org.ietf.nea.pt.validate.rules;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pt.validate.enums.PtTlsErrorCauseEnum;
import org.ietf.nea.pt.value.enums.PtTlsMessageErrorCodeEnum;

public class MessageVersion {

	public static void check(final short actualVersion, final short supportedVersion) throws RuleException{
        if(actualVersion != supportedVersion){
        	// the version parameter are added as reasons first actual, than supported. This is only by convention!
            throw new RuleException("The version "+actualVersion+" is not supported.",true,PtTlsMessageErrorCodeEnum.IETF_UNSUPPORTED_VERSION.code(),PtTlsErrorCauseEnum.MESSAGE_TYPE_NOT_SUPPORTED.number(),actualVersion, supportedVersion);
        }
    }
	
}
