package org.ietf.nea.pb.validate;

import org.ietf.nea.IETFConstants;
import org.ietf.nea.pb.message.PbMessage;
import org.ietf.nea.pb.message.enums.PbMessageTypeEnum;

import de.hsbremen.tc.tnc.tnccs.validate.TnccsValidator;

public class PbMessageValidatorFactroy {

	public static TnccsValidator<PbMessage> createDefault(){
		PbMessageValidator validator = new PbMessageValidator();
		validator.addValidator(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_ACCESS_RECOMMENDATION.messageType(), PbMessageAccessRecommendationValidator.getInstance());
		validator.addValidator(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_ASSESSMENT_RESULT.messageType(), PbMessageAssessmentResultValidator.getInstance());
		validator.addValidator(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_ERROR.messageType(), PbMessageErrorValidator.getInstance());
		validator.addValidator(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_EXPERIMENTAL.messageType(), PbMessageExperimentalValidator.getInstance());
		validator.addValidator(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_LANGUAGE_PREFERENCE.messageType(), PbMessageLanguagePreferenceValidator.getInstance());
		validator.addValidator(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_PA.messageType(), PbMessageImValidator.getInstance());
		validator.addValidator(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_REASON_STRING.messageType(), PbMessageReasonStringValidator.getInstance());
		validator.addValidator(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_REMEDIATION_PARAMETERS.messageType(), PbMessageRemediationParameterValidator.getInstance());
		
		return validator;
	}
	
}
