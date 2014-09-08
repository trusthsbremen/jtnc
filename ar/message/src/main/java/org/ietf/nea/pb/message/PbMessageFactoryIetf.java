package org.ietf.nea.pb.message;

import org.ietf.nea.IETFConstants;
import org.ietf.nea.pb.message.enums.PbMessageAccessRecommendationEnum;
import org.ietf.nea.pb.message.enums.PbMessageAssessmentResultEnum;
import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.message.enums.PbMessageErrorFlagsEnum;
import org.ietf.nea.pb.message.enums.PbMessageFlagsEnum;
import org.ietf.nea.pb.message.enums.PbMessageImFlagsEnum;
import org.ietf.nea.pb.message.enums.PbMessageRemediationParameterTypeEnum;
import org.ietf.nea.pb.message.enums.PbMessageTypeEnum;

import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;

public class PbMessageFactoryIetf {

	// TODO use the builder in the future
	private static long vendorId = IETFConstants.IETF_PEN_VENDORID;

	public static PbMessage createAccessRecommendation(PbMessageAccessRecommendationEnum recommendation) {
	
		short reserved = 0; // defined in RFC5793
		
		PbMessageFlagsEnum[] flags = new PbMessageFlagsEnum[0]; 
	    long type = PbMessageTypeEnum.IETF_PB_ACCESS_RECOMMENDATION.messageType();
	    
	    PbMessageBuilderIetf mBuilder = new PbMessageBuilderIetf();
	    PbMessage message = null;
		mBuilder.setFlags(flags);
		mBuilder.setVendorId(vendorId);
		mBuilder.setType(type);    
		mBuilder.setValue(PbMessageValueBuilderIetf.createAccessRecommendationValue(reserved, recommendation));
		try {
			message = mBuilder.toMessage();
		} catch (ValidationException e) {
			message = null;
			// TODO LOG
			e.printStackTrace();
		}
		
		return message;

	}

	public static PbMessage createAssessmentResult(PbMessageAssessmentResultEnum result) {

		PbMessageFlagsEnum[] flags = new PbMessageFlagsEnum[]{PbMessageFlagsEnum.NOSKIP};
	    long type = PbMessageTypeEnum.IETF_PB_ASSESSMENT_RESULT.messageType();
	    
	    PbMessageBuilderIetf mBuilder = new PbMessageBuilderIetf();
	    PbMessage message = null;
		mBuilder.setFlags(flags);
		mBuilder.setVendorId(vendorId);
		mBuilder.setType(type);    
		mBuilder.setValue(PbMessageValueBuilderIetf.createAssessmentResultValue(result));
		try {
			message = mBuilder.toMessage();
		} catch (ValidationException e) {
			message = null;
			// TODO LOG
			e.printStackTrace();
		}
	    
	    
		return message;

	}

	public static PbMessage createError(PbMessageErrorFlagsEnum[] errorFlags, PbMessageErrorCodeEnum errorCode,
			byte[] errorParameter) {
	
		short reserved = 0; // defined in RFC5793
		long errorVendorId = vendorId;
		
		PbMessageFlagsEnum[] flags = new PbMessageFlagsEnum[]{PbMessageFlagsEnum.NOSKIP};
	    long type = PbMessageTypeEnum.IETF_PB_ERROR.messageType();   				                  

	    PbMessageBuilderIetf mBuilder = new PbMessageBuilderIetf();
	    PbMessage message = null;
		mBuilder.setFlags(flags);
		mBuilder.setVendorId(vendorId);
		mBuilder.setType(type);    
		mBuilder.setValue(PbMessageValueBuilderIetf.createErrorValue(errorFlags, errorVendorId, errorCode.code(), reserved, errorParameter));
		try {
			message = mBuilder.toMessage();
		} catch (ValidationException e) {
			message = null;
			// TODO LOG
			e.printStackTrace();
		}
	    
	    return message;
	}

	public static PbMessage createExperimental(String content) {

		PbMessageFlagsEnum[] flags = new PbMessageFlagsEnum[0]; 	     
	    long type =  PbMessageTypeEnum.IETF_PB_EXPERIMENTAL.messageType();
		
	    PbMessageBuilderIetf mBuilder = new PbMessageBuilderIetf();
	    PbMessage message = null;
		mBuilder.setFlags(flags);
		mBuilder.setVendorId(vendorId);
		mBuilder.setType(type);    
		mBuilder.setValue(PbMessageValueBuilderIetf.createExperimentalValue(content));
		try {
			message = mBuilder.toMessage();
		} catch (ValidationException e) {
			message = null;
			// TODO LOG
			e.printStackTrace();
		}
	    
		return message;

	}

	public static PbMessage createIm(PbMessageImFlagsEnum[] imFlags, long subVendorId, long subType,
			short collectorId, short validatorId, byte[] imMessage) {

		PbMessageFlagsEnum[] flags = new PbMessageFlagsEnum[]{PbMessageFlagsEnum.NOSKIP};
		long type = PbMessageTypeEnum.IETF_PB_PA.messageType();
		
		PbMessageBuilderIetf mBuilder = new PbMessageBuilderIetf();
	    PbMessage message = null;
		mBuilder.setFlags(flags);
		mBuilder.setVendorId(vendorId);
		mBuilder.setType(type);    
		mBuilder.setValue(PbMessageValueBuilderIetf.createImValue(imFlags, subVendorId, subType, collectorId, validatorId, imMessage));
		try {
			message = mBuilder.toMessage();
		} catch (ValidationException e) {
			message = null;
			// TODO LOG
			e.printStackTrace();
		}
	    
		return message;

	}

	public static PbMessage createLanguagePreference(String preferedLanguage) {
		
		PbMessageFlagsEnum[] flags = new PbMessageFlagsEnum[0];
	    long type = PbMessageTypeEnum.IETF_PB_LANGUAGE_PREFERENCE.messageType();
	    
	    PbMessageBuilderIetf mBuilder = new PbMessageBuilderIetf();
	    PbMessage message = null;
		mBuilder.setFlags(flags);
		mBuilder.setVendorId(vendorId);
		mBuilder.setType(type);    
		mBuilder.setValue(PbMessageValueBuilderIetf.createLanguagePreferenceValue(preferedLanguage));
		try {
			message = mBuilder.toMessage();
		} catch (ValidationException e) {
			message = null;
			// TODO LOG
			e.printStackTrace();
		}
	    
		return message;

	}

	public static PbMessage createReasonString(String reasonString, String langCode) {
		
		PbMessageFlagsEnum[] flags = new PbMessageFlagsEnum[0];
	    long type = PbMessageTypeEnum.IETF_PB_REASON_STRING.messageType();
		
	    PbMessageBuilderIetf mBuilder = new PbMessageBuilderIetf();
	    PbMessage message = null;
		mBuilder.setFlags(flags);
		mBuilder.setVendorId(vendorId);
		mBuilder.setType(type);    
		mBuilder.setValue(PbMessageValueBuilderIetf.createReasonStringValue(reasonString, langCode));
		try {
			message = mBuilder.toMessage();
		} catch (ValidationException e) {
			message = null;
			// TODO LOG
			e.printStackTrace();
		}
	    
	    return message;

	}

	public static PbMessage createRemediationParameterString(String remediationString, String langCode) {
		
		byte reserved = 0; // defined in RFC5793
		long rpVendorId = vendorId; 
		long rpType = PbMessageRemediationParameterTypeEnum.IETF_STRING.type();
		
		PbMessageFlagsEnum[] flags = new PbMessageFlagsEnum[0];
	    long type = PbMessageTypeEnum.IETF_PB_REMEDIATION_PARAMETERS.messageType(); 
	    
	    PbMessageBuilderIetf mBuilder = new PbMessageBuilderIetf();
	    PbMessage message = null;
		mBuilder.setFlags(flags);
		mBuilder.setVendorId(vendorId);
		mBuilder.setType(type);    
		mBuilder.setValue(PbMessageValueBuilderIetf.createRemediationParameterString(reserved, rpVendorId, rpType, remediationString, langCode));
		try {
			message = mBuilder.toMessage();
		} catch (ValidationException e) {
			message = null;
			// TODO LOG
			e.printStackTrace();
		}
	    
		return message;

	}
	
	public static PbMessage createRemediationParameterUri(String uri) {
		
		byte reserved = 0; // defined in RFC5793
		long rpVendorId = vendorId; 
		long rpType = PbMessageRemediationParameterTypeEnum.IETF_URI.type();
		
		PbMessageFlagsEnum[] flags = new PbMessageFlagsEnum[0];
	    long type = PbMessageTypeEnum.IETF_PB_REMEDIATION_PARAMETERS.messageType(); 
	    
	    PbMessageBuilderIetf mBuilder = new PbMessageBuilderIetf();
	    PbMessage message = null;
		mBuilder.setFlags(flags);
		mBuilder.setVendorId(vendorId);
		mBuilder.setType(type);    
		mBuilder.setValue(PbMessageValueBuilderIetf.createRemediationParameterUri(reserved, rpVendorId, rpType, uri));
		try {
			message = mBuilder.toMessage();
		} catch (ValidationException e) {
			message = null;
			// TODO LOG
			e.printStackTrace();
		}
	    
		return message;

	}
}
