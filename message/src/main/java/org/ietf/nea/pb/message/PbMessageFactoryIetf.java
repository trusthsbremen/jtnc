package org.ietf.nea.pb.message;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pb.message.enums.PbMessageAccessRecommendationEnum;
import org.ietf.nea.pb.message.enums.PbMessageAssessmentResultEnum;
import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.message.enums.PbMessageErrorFlagsEnum;
import org.ietf.nea.pb.message.enums.PbMessageFlagsEnum;
import org.ietf.nea.pb.message.enums.PbMessageImFlagsEnum;
import org.ietf.nea.pb.message.enums.PbMessageRemediationParameterTypeEnum;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLength;
import org.ietf.nea.pb.message.enums.PbMessageTypeEnum;

import de.hsbremen.tc.tnc.IETFConstants;

public class PbMessageFactoryIetf {

	private static final long VENDORID = IETFConstants.IETF_PEN_VENDORID;

	public static PbMessage createAccessRecommendation(final PbMessageAccessRecommendationEnum recommendation) throws RuleException {
		
		byte flags = 0; 
	    long type = PbMessageTypeEnum.IETF_PB_ACCESS_RECOMMENDATION.messageType();
	    
	    return createMessage(flags, type,
	    		PbMessageValueBuilderIetf.createAccessRecommendationValue(recommendation));


	}

	public static PbMessage createAssessmentResult(final PbMessageAssessmentResultEnum result) throws RuleException {

		byte flags = PbMessageFlagsEnum.NOSKIP.bit();
	    long type = PbMessageTypeEnum.IETF_PB_ASSESSMENT_RESULT.messageType();
	    
	    return createMessage(flags, type, 
	    		PbMessageValueBuilderIetf.createAssessmentResultValue(result));

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
	
	public static PbMessage createErrorSimple(final PbMessageErrorFlagsEnum[] errorFlags, final PbMessageErrorCodeEnum errorCode) throws RuleException {
		
		long errorVendorId = VENDORID;
		
		byte flags = PbMessageFlagsEnum.NOSKIP.bit();
	    long type = PbMessageTypeEnum.IETF_PB_ERROR.messageType();   				                  

	    return createMessage(flags, type, 
	    		PbMessageValueBuilderIetf.createErrorValueSimple(errorFlags, errorVendorId, errorCode.code()));
	}
	
	public static PbMessage createErrorOffset(final PbMessageErrorFlagsEnum[] errorFlags, final PbMessageErrorCodeEnum errorCode, long offset) throws RuleException {
		
		long errorVendorId = VENDORID;
		
		byte flags = PbMessageFlagsEnum.NOSKIP.bit();
	    long type = PbMessageTypeEnum.IETF_PB_ERROR.messageType();   				                  

	    return createMessage(flags, type, 
	    		PbMessageValueBuilderIetf.createErrorValueWithOffset(errorFlags, errorVendorId, errorCode.code(), offset));
	}
	
	public static PbMessage createErrorVersion(final PbMessageErrorFlagsEnum[] errorFlags, final PbMessageErrorCodeEnum errorCode, short badVersion, short maxVersion, short minVersion) throws RuleException {
		
		long errorVendorId = VENDORID;
		
		byte flags = PbMessageFlagsEnum.NOSKIP.bit();
	    long type = PbMessageTypeEnum.IETF_PB_ERROR.messageType();   				                  

	    return createMessage(flags, type, 
	    		PbMessageValueBuilderIetf.createErrorValueWithVersion(errorFlags, errorVendorId, errorCode.code(), badVersion, maxVersion, minVersion));
	}
	
	public static PbMessage createRemediationParameterString(final String remediationString, final String langCode) throws RuleException {

		long rpVendorId = VENDORID; 
		long rpType = PbMessageRemediationParameterTypeEnum.IETF_STRING.type();
		
		byte flags = 0;
	    long type = PbMessageTypeEnum.IETF_PB_REMEDIATION_PARAMETERS.messageType(); 
	    
	    return createMessage(flags, type, 
			  PbMessageValueBuilderIetf.createRemediationParameterString(rpVendorId, rpType, remediationString, langCode));
		
	}
	
	public static PbMessage createRemediationParameterUri(final String uri) throws RuleException {

		long rpVendorId = VENDORID; 
		long rpType = PbMessageRemediationParameterTypeEnum.IETF_URI.type();
		
		byte flags = 0;
	    long type = PbMessageTypeEnum.IETF_PB_REMEDIATION_PARAMETERS.messageType(); 
	    
	    return createMessage(flags, type, 
	    		PbMessageValueBuilderIetf.createRemediationParameterUri(rpVendorId, rpType, uri));

	}
	
	// TODO what do we do with errors
	private static PbMessage createMessage(final byte flags, final long type, final AbstractPbMessageValue value) throws RuleException {
		if(value == null){
			throw new NullPointerException("Value cannot be null.");
		}
		
	    PbMessageHeaderBuilderIetf mBuilder = new PbMessageHeaderBuilderIetf();
		mBuilder.setFlags(flags);
		mBuilder.setVendorId(VENDORID);
		mBuilder.setType(type);
		mBuilder.setLength(PbMessageTlvFixedLength.MESSAGE.length() + value.getLength());

		PbMessage message = new PbMessage(mBuilder.toMessageHeader(), value);
		
		return message;

	}
}
