package org.ietf.nea.pb.validate.rules;

import org.ietf.nea.pb.message.enums.PbMessageAssessmentResultEnum;
import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.validate.enums.PbErrorCauseEnum;

import de.hsbremen.tc.tnc.message.exception.RuleException;

public class AssessmentResult {

	public static void check(final long result) throws RuleException{
		if(PbMessageAssessmentResultEnum.fromId(result) == null){
        	throw new RuleException("The result value " + result + " is unknown.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PbErrorCauseEnum.ASSESSMENT_RESULT_NOT_SUPPORTED.number(),result);
        }
    }
	
}
