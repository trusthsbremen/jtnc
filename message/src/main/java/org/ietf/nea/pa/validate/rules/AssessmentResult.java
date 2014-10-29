package org.ietf.nea.pa.validate.rules;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pa.attribute.enums.PaAttributeAssessmentResultEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeErrorCodeEnum;
import org.ietf.nea.pa.validate.enums.PaErrorCauseEnum;

public class AssessmentResult {

	public static void check(final long result) throws RuleException{
		if(PaAttributeAssessmentResultEnum.fromNumber(result) == null){
        	throw new RuleException("The type value " + result + " is unknown.",false,PaAttributeErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PaErrorCauseEnum.ASSESSMENT_RESULT_NOT_SUPPORTED.number(),Long.toString(result));
        }
    }
	
}
