package org.ietf.nea.pb.message;

import java.net.URI;

import org.ietf.nea.IETFConstants;
import org.ietf.nea.pb.message.enums.PbMessageAccessRecommendationEnum;
import org.ietf.nea.pb.message.enums.PbMessageAssessmentResultEnum;
import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.message.enums.PbMessageErrorFlagsEnum;
import org.ietf.nea.pb.message.enums.PbMessageFlagsEnum;
import org.ietf.nea.pb.message.enums.PbMessageImFlagsEnum;
import org.ietf.nea.pb.message.enums.PbMessageRemediationParameterTypeEnum;
import org.ietf.nea.pb.message.enums.PbMessageTypeEnum;

public class PbMessageFactoryIetf {

	private static long vendorId = IETFConstants.IETF_PEN_VENDORID;

	public static PbMessage createAccessRecommendation(PbMessageAccessRecommendationEnum recommendation) {
		
		if(recommendation == null){
			throw new NullPointerException("Recommendation can not be null.");
		}
		short reserved = 0; // defined in RFC5793
		AbstractPbMessageValue value = new PbMessageValueAccessRecommendation(reserved, recommendation);
		
		PbMessageFlagsEnum[] flags = new PbMessageFlagsEnum[0]; 
	    long type = PbMessageTypeEnum.IETF_PB_ACCESS_RECOMMENDATION.messageType();
	    
		return new PbMessage(flags, vendorId, type, value);

	}

	public static PbMessage createAssessmentResult(PbMessageAssessmentResultEnum result) {
		if(result == null){
			throw new NullPointerException("Assesment result can not be null.");
		}
		AbstractPbMessageValue value = new PbMessageValueAssessmentResult(result);
		
		PbMessageFlagsEnum[] flags = new PbMessageFlagsEnum[]{PbMessageFlagsEnum.NOSKIP};
	    long type = PbMessageTypeEnum.IETF_PB_ASSESSMENT_RESULT.messageType();
	    
		return new PbMessage(flags, vendorId, type, value);

	}

	public static PbMessage createError(PbMessageErrorFlagsEnum[] errorFlags, long errorVendorId,
			PbMessageErrorCodeEnum errorCode,
			byte[] errorParameter) {
		
		if(errorFlags == null){
			errorFlags = new PbMessageErrorFlagsEnum[0];
		}
		if(errorParameter == null){
			errorParameter = new byte[0];
		}
		if(errorCode == null){
			throw new NullPointerException("Error code can not be null.");
		}		
		short reserved = 0; // defined in RFC5793
		AbstractPbMessageValue value = new PbMessageValueError(errorFlags, errorVendorId, errorCode, reserved, errorParameter);
		
		PbMessageFlagsEnum[] flags = new PbMessageFlagsEnum[]{PbMessageFlagsEnum.NOSKIP};
	    long type = PbMessageTypeEnum.IETF_PB_ERROR.messageType();   				                  

	    return new PbMessage(flags, vendorId, type, value);
	}

	public static PbMessage createExperimental(String content) {
		if(content == null){
			content = "";
		}
		AbstractPbMessageValue value = new PbMessageValueExperimental(content);
		
		PbMessageFlagsEnum[] flags = new PbMessageFlagsEnum[0]; 	     
	    long type =  PbMessageTypeEnum.IETF_PB_EXPERIMENTAL.messageType();
		
		return new PbMessage(flags, vendorId, type, value);

	}

	public static PbMessage createIm(PbMessageImFlagsEnum[] imFlags, long subVendorId, long subType,
			long collectorId, long validatorId, byte[] message) {
		
		if(imFlags == null){
			imFlags = new PbMessageImFlagsEnum[0];
		}
		if(message == null){
			message = new byte[0];
		}
		AbstractPbMessageValue value = new PbMessageValueIm(imFlags, subVendorId, subType, collectorId, validatorId, message);

		PbMessageFlagsEnum[] flags = new PbMessageFlagsEnum[]{PbMessageFlagsEnum.NOSKIP};
		long type = PbMessageTypeEnum.IETF_PB_PA.messageType();
	    
		return new PbMessage(flags, vendorId, type, value);

	}

	public static PbMessage createLanguagePreference(String preferedLanguage) {
		if(preferedLanguage == null){
			preferedLanguage = "";
		}
		AbstractPbMessageValue value = new PbMessageValueLanguagePreference(preferedLanguage);

		PbMessageFlagsEnum[] flags = new PbMessageFlagsEnum[0];
	    long type = PbMessageTypeEnum.IETF_PB_LANGUAGE_PREFERENCE.messageType();
	    
		return new PbMessage(flags, vendorId, type, value);

	}

	public static PbMessage createReasonString(String reasonString, String langCode) {
		if(reasonString == null){
			reasonString = "";
		}
		if(langCode == null){
			langCode = "";
		}
		if(langCode.getBytes().length > 255){
			// Language code length is a 8 bit(s) field, therefore the language code may only be 255 byte long (defined in RFC5793).   
			throw new IllegalArgumentException("Language code with length "+ langCode.getBytes().length +" is to long. Maximum size is 255.");
		}
		AbstractPbMessageValue value = new PbMessageValueReasonString(reasonString, langCode);
		
		PbMessageFlagsEnum[] flags = new PbMessageFlagsEnum[0];
	    long type = PbMessageTypeEnum.IETF_PB_REASON_STRING.messageType();
		
	    return new PbMessage(flags, vendorId, type, value);

	}

	public static PbMessage createRemediationParameterString(String remediationString, String langCode) {
		if(remediationString == null){
			remediationString = "";
		}
		if(langCode == null){
			langCode = "";
		}
		if(langCode.getBytes().length > 255){
			// Language code length is a 8 bit(s) field, therefore the language code may only be 255 byte long (defined in RFC5793).   
			throw new IllegalArgumentException("Language code with length "+ langCode.getBytes().length +" is to long. Maximum size is 255.");
		}
		
		byte reserved = 0; // defined in RFC5793
		long rpVendorId = vendorId; 
		long rpType = PbMessageRemediationParameterTypeEnum.IETF_STRING.type();
		AbstractPbMessageValueRemediationParametersValue parameter = 
				new PbMessageValueRemediationParameterString(remediationString, langCode);
		AbstractPbMessageValue value = 
				new PbMessageValueRemediationParameters(reserved, rpVendorId, rpType, parameter);
		
		PbMessageFlagsEnum[] flags = new PbMessageFlagsEnum[0];
	    long type = PbMessageTypeEnum.IETF_PB_REMEDIATION_PARAMETERS.messageType(); 
	    
		return new PbMessage(flags, vendorId, type, value);

	}
	
	public static PbMessage createRemediationParameterUri(URI remediationUri) {
		if(remediationUri == null){
			throw new NullPointerException("URI can not be null.");
		}
		byte reserved = 0; // defined in RFC5793
		long rpVendorId = vendorId; 
		long rpType = PbMessageRemediationParameterTypeEnum.IETF_URI.type();
		AbstractPbMessageValueRemediationParametersValue parameter = 
				new PbMessageValueRemediationParameterUri(remediationUri);
		AbstractPbMessageValue value = 
				new PbMessageValueRemediationParameters(reserved, rpVendorId, rpType, parameter);
		
		PbMessageFlagsEnum[] flags = new PbMessageFlagsEnum[0];
	    long type = PbMessageTypeEnum.IETF_PB_REMEDIATION_PARAMETERS.messageType(); 
	    
		return new PbMessage(flags, vendorId, type, value);

	}
}
