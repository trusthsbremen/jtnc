package org.ietf.nea.pb.validate.rules;

import org.ietf.nea.pb.message.enums.PbMessageAccessRecommendationEnum;
import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.validate.enums.PbErrorCauseEnum;

import de.hsbremen.tc.tnc.message.exception.RuleException;

public class AccessRecommendation {

	public static void check(final int recommendation) throws RuleException{
		if(PbMessageAccessRecommendationEnum.fromNumber(recommendation) == null){
        	throw new RuleException("The recommendation value " + recommendation + " is unknown.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PbErrorCauseEnum.ACCESS_RECOMMENDATION_NOT_SUPPORTED.number(),recommendation);
        }
    }
	
}
