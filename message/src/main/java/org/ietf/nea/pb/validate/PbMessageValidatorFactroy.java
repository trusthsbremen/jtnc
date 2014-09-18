package org.ietf.nea.pb.validate;

import org.ietf.nea.pb.message.PbMessage;
import org.ietf.nea.pb.message.enums.PbMessageTypeEnum;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.tnccs.validate.TnccsValidator;

public class PbMessageValidatorFactroy {

	public static TnccsValidator<PbMessage> createDefault(){
		PbMessageValidator validator = new PbMessageValidator();
		validator.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_ACCESS_RECOMMENDATION.messageType(), PbMessageAccessRecommendationValidator.getInstance());
		validator.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_ASSESSMENT_RESULT.messageType(), PbMessageAssessmentResultValidator.getInstance());
		validator.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_ERROR.messageType(), PbMessageErrorValidator.getInstance());
		validator.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_EXPERIMENTAL.messageType(), PbMessageExperimentalValidator.getInstance());
		validator.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_LANGUAGE_PREFERENCE.messageType(), PbMessageLanguagePreferenceValidator.getInstance());
		validator.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_PA.messageType(), PbMessageImValidator.getInstance());
		validator.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_REASON_STRING.messageType(), PbMessageReasonStringValidator.getInstance());
		validator.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_REMEDIATION_PARAMETERS.messageType(), PbMessageRemediationParameterValidator.getInstance());
		
		return validator;
	}
	
}
