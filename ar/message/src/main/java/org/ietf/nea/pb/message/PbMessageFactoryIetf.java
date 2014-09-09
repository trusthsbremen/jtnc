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

	private static long vendorId = IETFConstants.IETF_PEN_VENDORID;

	public static PbMessage createAccessRecommendation(final PbMessageAccessRecommendationEnum recommendation) {
	
		short reserved = 0; // defined in RFC5793
		
		PbMessageFlagsEnum[] flags = new PbMessageFlagsEnum[0]; 
	    long type = PbMessageTypeEnum.IETF_PB_ACCESS_RECOMMENDATION.messageType();
	    
	    return createMessage(flags, type,
	    		PbMessageValueBuilderIetf.createAccessRecommendationValue(reserved, recommendation));


	}

	public static PbMessage createAssessmentResult(final PbMessageAssessmentResultEnum result) {

		PbMessageFlagsEnum[] flags = new PbMessageFlagsEnum[]{PbMessageFlagsEnum.NOSKIP};
	    long type = PbMessageTypeEnum.IETF_PB_ASSESSMENT_RESULT.messageType();
	    
	    return createMessage(flags, type, 
	    		PbMessageValueBuilderIetf.createAssessmentResultValue(result));

	}

	public static PbMessage createError(final PbMessageErrorFlagsEnum[] errorFlags, final PbMessageErrorCodeEnum errorCode,
			final byte[] errorParameter) {
	
		short reserved = 0; // defined in RFC5793
		long errorVendorId = vendorId;
		
		PbMessageFlagsEnum[] flags = new PbMessageFlagsEnum[]{PbMessageFlagsEnum.NOSKIP};
	    long type = PbMessageTypeEnum.IETF_PB_ERROR.messageType();   				                  

	    return createMessage(flags, type, 
	    		PbMessageValueBuilderIetf.createErrorValue(errorFlags, errorVendorId, errorCode.code(), reserved, errorParameter));
	}

	public static PbMessage createExperimental(final String content) {

		PbMessageFlagsEnum[] flags = new PbMessageFlagsEnum[0]; 	     
	    long type =  PbMessageTypeEnum.IETF_PB_EXPERIMENTAL.messageType();
		
	    return createMessage(flags, type, 
	    		PbMessageValueBuilderIetf.createExperimentalValue(content));

	}

	public static PbMessage createIm(final PbMessageImFlagsEnum[] imFlags, final long subVendorId, final long subType,
			final short collectorId, final short validatorId, final byte[] imMessage) {

		PbMessageFlagsEnum[] flags = new PbMessageFlagsEnum[]{PbMessageFlagsEnum.NOSKIP};
		long type = PbMessageTypeEnum.IETF_PB_PA.messageType();
		
		return createMessage(flags, type, 
				PbMessageValueBuilderIetf.createImValue(imFlags, subVendorId, subType, collectorId, validatorId, imMessage));

	}

	public static PbMessage createLanguagePreference(final String preferedLanguage) {
		
		PbMessageFlagsEnum[] flags = new PbMessageFlagsEnum[0];
	    long type = PbMessageTypeEnum.IETF_PB_LANGUAGE_PREFERENCE.messageType();
	    
	    return createMessage(flags, type, 
	    		PbMessageValueBuilderIetf.createLanguagePreferenceValue(preferedLanguage));

	}

	public static PbMessage createReasonString(final String reasonString, final String langCode) {
		
		PbMessageFlagsEnum[] flags = new PbMessageFlagsEnum[0];
	    long type = PbMessageTypeEnum.IETF_PB_REASON_STRING.messageType();
		
	    return createMessage(flags, type,
			   PbMessageValueBuilderIetf.createReasonStringValue(reasonString, langCode));

	}

	public static PbMessage createRemediationParameterString(final String remediationString, final String langCode) {
		
		byte reserved = 0; // defined in RFC5793
		long rpVendorId = vendorId; 
		long rpType = PbMessageRemediationParameterTypeEnum.IETF_STRING.type();
		
		PbMessageFlagsEnum[] flags = new PbMessageFlagsEnum[0];
	    long type = PbMessageTypeEnum.IETF_PB_REMEDIATION_PARAMETERS.messageType(); 
	    
	    return createMessage(flags, type, 
			  PbMessageValueBuilderIetf.createRemediationParameterString(reserved, rpVendorId, rpType, remediationString, langCode));
		
	}
	
	public static PbMessage createRemediationParameterUri(final String uri) {
		
		byte reserved = 0; // defined in RFC5793
		long rpVendorId = vendorId; 
		long rpType = PbMessageRemediationParameterTypeEnum.IETF_URI.type();
		
		PbMessageFlagsEnum[] flags = new PbMessageFlagsEnum[0];
	    long type = PbMessageTypeEnum.IETF_PB_REMEDIATION_PARAMETERS.messageType(); 
	    
	    return createMessage(flags, type, 
	    		PbMessageValueBuilderIetf.createRemediationParameterUri(reserved, rpVendorId, rpType, uri));

	}
	
	private static PbMessage createMessage(final PbMessageFlagsEnum[] flags, final long type, final AbstractPbMessageValue value) {
	    
	    PbMessageBuilderIetf mBuilder = new PbMessageBuilderIetf();
	    PbMessage message = null;
		mBuilder.setFlags(flags);
		mBuilder.setVendorId(vendorId);
		mBuilder.setType(type);    
		mBuilder.setValue(value);
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
