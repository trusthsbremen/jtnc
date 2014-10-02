package org.ietf.nea.pb.message.factory;

import org.ietf.nea.pb.exception.RuleException;
import org.ietf.nea.pb.message.AbstractPbMessageValue;
import org.ietf.nea.pb.message.PbMessage;
import org.ietf.nea.pb.message.PbMessageBuilderIetf;
import org.ietf.nea.pb.message.PbMessageValueBuilderIetf;
import org.ietf.nea.pb.message.enums.PbMessageAccessRecommendationEnum;
import org.ietf.nea.pb.message.enums.PbMessageAssessmentResultEnum;
import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.message.enums.PbMessageErrorFlagsEnum;
import org.ietf.nea.pb.message.enums.PbMessageFlagsEnum;
import org.ietf.nea.pb.message.enums.PbMessageImFlagsEnum;
import org.ietf.nea.pb.message.enums.PbMessageRemediationParameterTypeEnum;
import org.ietf.nea.pb.message.enums.PbMessageTypeEnum;

import de.hsbremen.tc.tnc.IETFConstants;

public class PbMessageFactoryIetf {

	private static long vendorId = IETFConstants.IETF_PEN_VENDORID;

	public static PbMessage createAccessRecommendation(final PbMessageAccessRecommendationEnum recommendation) throws RuleException {
	
		short reserved = 0; // defined in RFC5793
		
		byte flags = 0; 
	    long type = PbMessageTypeEnum.IETF_PB_ACCESS_RECOMMENDATION.messageType();
	    
	    return createMessage(flags, type,
	    		PbMessageValueBuilderIetf.createAccessRecommendationValue(reserved, recommendation));


	}

	public static PbMessage createAssessmentResult(final PbMessageAssessmentResultEnum result) throws RuleException {

		byte flags = PbMessageFlagsEnum.NOSKIP.bit();
	    long type = PbMessageTypeEnum.IETF_PB_ASSESSMENT_RESULT.messageType();
	    
	    return createMessage(flags, type, 
	    		PbMessageValueBuilderIetf.createAssessmentResultValue(result));

	}

	public static PbMessage createError(final PbMessageErrorFlagsEnum[] errorFlags, final PbMessageErrorCodeEnum errorCode,
			final byte[] errorParameter) throws RuleException {
	
		short reserved = 0; // defined in RFC5793
		long errorVendorId = vendorId;
		
		byte flags = PbMessageFlagsEnum.NOSKIP.bit();
	    long type = PbMessageTypeEnum.IETF_PB_ERROR.messageType();   				                  

	    return createMessage(flags, type, 
	    		PbMessageValueBuilderIetf.createErrorValue(errorFlags, errorVendorId, errorCode.code(), reserved, errorParameter));
	}

	public static PbMessage createExperimental(final String content) throws RuleException {

		byte flags = 0; 	     
	    long type =  PbMessageTypeEnum.IETF_PB_EXPERIMENTAL.messageType();
		
	    return createMessage(flags, type, 
	    		PbMessageValueBuilderIetf.createExperimentalValue(content));

	}

	public static PbMessage createIm(final PbMessageImFlagsEnum[] imFlags, final long subVendorId, final long subType,
			final short collectorId, final short validatorId, final byte[] imMessage) throws RuleException {

		byte flags = PbMessageFlagsEnum.NOSKIP.bit();
		long type = PbMessageTypeEnum.IETF_PB_PA.messageType();
		
		return createMessage(flags, type, 
				PbMessageValueBuilderIetf.createImValue(imFlags, subVendorId, subType, collectorId, validatorId, imMessage));

	}

	public static PbMessage createLanguagePreference(final String preferedLanguage) throws RuleException {
		
		byte flags = 0;
	    long type = PbMessageTypeEnum.IETF_PB_LANGUAGE_PREFERENCE.messageType();
	    
	    return createMessage(flags, type, 
	    		PbMessageValueBuilderIetf.createLanguagePreferenceValue(preferedLanguage));

	}

	public static PbMessage createReasonString(final String reasonString, final String langCode) throws RuleException {
		
		byte flags = 0;
	    long type = PbMessageTypeEnum.IETF_PB_REASON_STRING.messageType();
		
	    return createMessage(flags, type,
			   PbMessageValueBuilderIetf.createReasonStringValue(reasonString, langCode));

	}

	public static PbMessage createRemediationParameterString(final String remediationString, final String langCode) throws RuleException {
		
		byte reserved = 0; // defined in RFC5793
		long rpVendorId = vendorId; 
		long rpType = PbMessageRemediationParameterTypeEnum.IETF_STRING.type();
		
		byte flags = 0;
	    long type = PbMessageTypeEnum.IETF_PB_REMEDIATION_PARAMETERS.messageType(); 
	    
	    return createMessage(flags, type, 
			  PbMessageValueBuilderIetf.createRemediationParameterString(reserved, rpVendorId, rpType, remediationString, langCode));
		
	}
	
	public static PbMessage createRemediationParameterUri(final String uri) throws RuleException {
		
		byte reserved = 0; // defined in RFC5793
		long rpVendorId = vendorId; 
		long rpType = PbMessageRemediationParameterTypeEnum.IETF_URI.type();
		
		byte flags = 0;
	    long type = PbMessageTypeEnum.IETF_PB_REMEDIATION_PARAMETERS.messageType(); 
	    
	    return createMessage(flags, type, 
	    		PbMessageValueBuilderIetf.createRemediationParameterUri(reserved, rpVendorId, rpType, uri));

	}
	
	// TODO what do we do with errors
	private static PbMessage createMessage(final byte flags, final long type, final AbstractPbMessageValue value) throws RuleException {
	    
	    PbMessageBuilderIetf mBuilder = new PbMessageBuilderIetf();
	    PbMessage message = null;
		mBuilder.setFlags(flags);
		mBuilder.setVendorId(vendorId);
		mBuilder.setType(type);    
		mBuilder.setValue(value);

		message = mBuilder.toMessage();
		
		return message;

	}
}
