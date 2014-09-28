package org.ietf.nea.pb.validate.rules;

import org.ietf.nea.pb.message.enums.PbMessageAccessRecommendationEnum;
import org.ietf.nea.pb.message.enums.PbMessageAssessmentResultEnum;
import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.validate.enums.PbErrorCauseEnum;

import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;

public class AccessRecommendation {

	public static void check(final short recommendation) throws ValidationException{
		if(PbMessageAccessRecommendationEnum.fromNumber(recommendation) == null){
        	throw new ValidationException("The type value " + recommendation + " is unknown.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PbErrorCauseEnum.ACCESS_RECOMMENDATION_NOT_SUPPORTED.number(),Short.toString(recommendation));
        }
    }
	
}
