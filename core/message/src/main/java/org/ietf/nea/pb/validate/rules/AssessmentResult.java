package org.ietf.nea.pb.validate.rules;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pb.message.enums.PbMessageAssessmentResultEnum;
import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.validate.enums.PbErrorCauseEnum;

public class AssessmentResult {

	public static void check(final long result) throws RuleException{
		if(PbMessageAssessmentResultEnum.fromNumber(result) == null){
        	throw new RuleException("The result value " + result + " is unknown.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PbErrorCauseEnum.ASSESSMENT_RESULT_NOT_SUPPORTED.number(),result);
        }
    }
	
}
