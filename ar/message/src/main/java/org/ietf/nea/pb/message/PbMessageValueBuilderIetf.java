package org.ietf.nea.pb.message;

import java.net.URI;

import org.ietf.nea.IETFConstants;
import org.ietf.nea.pb.message.enums.PbMessageAccessRecommendationEnum;
import org.ietf.nea.pb.message.enums.PbMessageAssessmentResultEnum;
import org.ietf.nea.pb.message.enums.PbMessageErrorFlagsEnum;
import org.ietf.nea.pb.message.enums.PbMessageImFlagsEnum;
import org.ietf.nea.pb.message.enums.PbMessageRemediationParameterTypeEnum;

public class PbMessageValueBuilderIetf {
	
	public static PbMessageValueIm createImValue(final PbMessageImFlagsEnum[] imFlags, final long subVendorId, final long subType, final short collectorId, final short validatorId, final byte[] message){

		if(imFlags == null){
			throw new NullPointerException("Supplied flags cannot be null.");
		}
		if(message == null){
			throw new NullPointerException("Supplied message array cannot be null.");
		}
		
		if(subVendorId > IETFConstants.IETF_MAX_VENDOR_ID){
			throw new IllegalArgumentException("Vendor ID is greater than "+ Long.toString(IETFConstants.IETF_MAX_VENDOR_ID) + ".");
		}
		if(subType > IETFConstants.IETF_MAX_TYPE){
			throw new IllegalArgumentException("Type is greater than "+ Long.toString(IETFConstants.IETF_MAX_TYPE) + ".");
		}

		return new PbMessageValueIm(imFlags, subVendorId, subType, collectorId, validatorId, message);
	}
	
	public static PbMessageValueAccessRecommendation createAccessRecommendationValue(final short reserved, final PbMessageAccessRecommendationEnum recommendation){
		
		short reservedIgnored = 0;
		
		if(recommendation == null){
			throw new NullPointerException("Recommendation cannot be null.");
		}
		
		return new PbMessageValueAccessRecommendation(reservedIgnored, recommendation);
	}
	
	public static PbMessageValueAssessmentResult createAssessmentResultValue(final PbMessageAssessmentResultEnum result){
		
		if(result == null){
			throw new NullPointerException("Result cannot be null.");
		}
		
		return new PbMessageValueAssessmentResult(result);
	}
	
	public static PbMessageValueError createErrorValue(final PbMessageErrorFlagsEnum[] errorFlags, final long errorVendorId, final short errorCode, final short reserved, final byte[] errorParameter){
	
		short reservedIgnored = 0;
		
		if(errorFlags == null){
			throw new NullPointerException("Error flags cannot be null.");
		}
		if(errorParameter == null){
			throw new NullPointerException("Error parameter cannot be null.");
		}
		
		if(errorVendorId > IETFConstants.IETF_MAX_VENDOR_ID){
			throw new IllegalArgumentException("Vendor ID is greater than "+ Long.toString(IETFConstants.IETF_MAX_VENDOR_ID) + ".");
		}
		
		return new PbMessageValueError(errorFlags, errorVendorId, errorCode, reservedIgnored, errorParameter);
		
	}
	
	public static PbMessageValueLanguagePreference createLanguagePreferenceValue(final String preferedLanguage){
		
		if(preferedLanguage == null){
			throw new NullPointerException("Result cannot be null.");
		}
		
		return new PbMessageValueLanguagePreference(preferedLanguage);
		
	}
	
	public static PbMessageValueExperimental createExperimentalValue(final String message){
		
		if(message == null){
			throw new NullPointerException("Result cannot be null.");
		}
		
		return new PbMessageValueExperimental(message);
	}
	
	public static PbMessageValueReasonString createReasonStringValue(final String reasonString, final String langCode){
		
		if(reasonString == null){
			throw new NullPointerException("Reason string cannot be null.");
		}
		if(langCode == null){
			throw new NullPointerException("Language code cannot be null.");
		}
		if(langCode.length() > 0xFF){
			throw new IllegalArgumentException("Language code length " +langCode.length()+ "is to long.");
		}

		return new PbMessageValueReasonString(reasonString, langCode);
	}
	
	public static PbMessageValueRemediationParameters createRemediationParameterString(final byte reserved, final long rpVendorId, final long rpType, final String remediationString, final String langCode){
	
		if(remediationString == null){
			throw new NullPointerException("Remediation string cannot be null.");
		}
		if(langCode == null){
			throw new NullPointerException("Language code cannot be null.");
		}
		if(langCode.length() > 0xFF){
			throw new IllegalArgumentException("Language code length " +langCode.length()+ "is to long.");
		}
		if(rpVendorId != IETFConstants.IETF_PEN_VENDORID || rpType != PbMessageRemediationParameterTypeEnum.IETF_STRING.type()){
			throw new IllegalArgumentException("Requested remediation value is not supported in message with remediation vendor ID "+ rpVendorId +" and of remediation type "+ rpType +".");
		}

		PbMessageValueRemediationParameterString parameter = new PbMessageValueRemediationParameterString(remediationString, langCode);
		
		return createRemediationParameter(reserved, rpVendorId, rpType, parameter);
	}
	
	public static PbMessageValueRemediationParameters createRemediationParameterUri(final byte reserved, final long rpVendorId, final long rpType, String uri){
		
		if(uri == null){
			throw new NullPointerException("URI cannot be null.");
		}
		if(rpVendorId != IETFConstants.IETF_PEN_VENDORID || rpType != PbMessageRemediationParameterTypeEnum.IETF_URI.type()){
			throw new IllegalArgumentException("Requested remediation value is not supported in message with remediation vendor ID "+ rpVendorId +" and of remediation type "+ rpType +".");
		}
		
		PbMessageValueRemediationParameterUri parameter = new PbMessageValueRemediationParameterUri(URI.create(uri));
		
		return createRemediationParameter(reserved, rpVendorId, rpType, parameter);
		
	}
	
	private static PbMessageValueRemediationParameters createRemediationParameter(final byte reserved, final long rpVendorId, final long rpType, final AbstractPbMessageValueRemediationParametersValue parameter){
		byte reservedIgnored = 0;
		
		if(rpVendorId > IETFConstants.IETF_MAX_VENDOR_ID){
			throw new IllegalArgumentException("Vendor ID is greater than "+ Long.toString(IETFConstants.IETF_MAX_VENDOR_ID) + ".");
		}
		if(rpType > IETFConstants.IETF_MAX_TYPE){
			throw new IllegalArgumentException("Type is greater than "+ Long.toString(IETFConstants.IETF_MAX_TYPE) + ".");
		}

		return new PbMessageValueRemediationParameters(reservedIgnored, rpVendorId, rpType, parameter);
	}
	
}
