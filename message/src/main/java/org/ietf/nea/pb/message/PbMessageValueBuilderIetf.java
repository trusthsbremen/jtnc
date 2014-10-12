package org.ietf.nea.pb.message;

import java.net.URI;
import java.nio.charset.Charset;

import org.ietf.nea.pb.message.enums.PbMessageAccessRecommendationEnum;
import org.ietf.nea.pb.message.enums.PbMessageAssessmentResultEnum;
import org.ietf.nea.pb.message.enums.PbMessageErrorFlagsEnum;
import org.ietf.nea.pb.message.enums.PbMessageImFlagsEnum;
import org.ietf.nea.pb.message.enums.PbMessageRemediationParameterTypeEnum;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLength;

import de.hsbremen.tc.tnc.IETFConstants;

public class PbMessageValueBuilderIetf {
	
	public static PbMessageValueIm createImValue(final PbMessageImFlagsEnum[] imFlags, final long subVendorId, final long subType, final short collectorId, final short validatorId, final byte[] message){

		if(message == null){
			throw new NullPointerException("Supplied message array cannot be null.");
		}
		
		if(subVendorId > IETFConstants.IETF_MAX_VENDOR_ID){
			throw new IllegalArgumentException("Vendor ID is greater than "+ Long.toString(IETFConstants.IETF_MAX_VENDOR_ID) + ".");
		}
		if(subType > IETFConstants.IETF_MAX_TYPE){
			throw new IllegalArgumentException("Type is greater than "+ Long.toString(IETFConstants.IETF_MAX_TYPE) + ".");
		}

		long length = PbMessageTlvFixedLength.IM_VALUE.length() + message.length;
		
		return new PbMessageValueIm(imFlags, subVendorId, subType, collectorId, validatorId, length, message);
	}
	
	public static PbMessageValueAccessRecommendation createAccessRecommendationValue(final PbMessageAccessRecommendationEnum recommendation){
		
		if(recommendation == null){
			throw new NullPointerException("Recommendation cannot be null.");
		}
		
		return new PbMessageValueAccessRecommendation(PbMessageTlvFixedLength.ACC_REC_VALUE.length(),recommendation);
	}
	
	public static PbMessageValueAssessmentResult createAssessmentResultValue(final PbMessageAssessmentResultEnum result){
		
		if(result == null){
			throw new NullPointerException("Result cannot be null.");
		}
		
		return new PbMessageValueAssessmentResult(PbMessageTlvFixedLength.ASS_RES_VALUE.length(),result);
	}
	
	public static PbMessageValueError createErrorValue(final PbMessageErrorFlagsEnum[] errorFlags, final long errorVendorId, final short errorCode, final byte[] errorParameter){
		
		if(errorFlags == null){
			throw new NullPointerException("Error flags cannot be null.");
		}
		if(errorParameter == null){
			throw new NullPointerException("Error parameter cannot be null.");
		}
		
		if(errorVendorId > IETFConstants.IETF_MAX_VENDOR_ID){
			throw new IllegalArgumentException("Vendor ID is greater than "+ Long.toString(IETFConstants.IETF_MAX_VENDOR_ID) + ".");
		}
		
		long length = PbMessageTlvFixedLength.ERR_VALUE.length() + errorParameter.length;
		
		return new PbMessageValueError(errorFlags, errorVendorId, errorCode, length, errorParameter);
		
	}
	
	public static PbMessageValueLanguagePreference createLanguagePreferenceValue(final String preferedLanguage){
		
		if(preferedLanguage == null){
			throw new NullPointerException("Result cannot be null.");
		}
		
		long length = preferedLanguage.getBytes(Charset.forName("US-ASCII")).length;
		
		return new PbMessageValueLanguagePreference(length,preferedLanguage);
		
	}
	
	public static PbMessageValueExperimental createExperimentalValue(final String message){
		
		if(message == null){
			throw new NullPointerException("Result cannot be null.");
		}
		
		return new PbMessageValueExperimental(message.length(),message);
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

		long length = PbMessageTlvFixedLength.REA_STR_VALUE.length();
		if(reasonString.length() > 0){
			length += reasonString.getBytes(Charset.forName("UTF-8")).length;
		}
		if(langCode.length() > 0){
			length += langCode.getBytes(Charset.forName("US-ASCII")).length;
		}
		
		return new PbMessageValueReasonString(length, reasonString, langCode);
	}
	
	public static PbMessageValueRemediationParameters createRemediationParameterString(final long rpVendorId, final long rpType, final String remediationString, final String langCode){
	
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
		
		long length = PbMessageTlvFixedLength.REM_STR_SUB_VALUE.length();
		if(remediationString.length() > 0){
			length += remediationString.getBytes(Charset.forName("UTF-8")).length;
		}
		if(langCode.length() > 0){
			length += langCode.getBytes(Charset.forName("US-ASCII")).length;
		}
		
		PbMessageValueRemediationParameterString parameter = new PbMessageValueRemediationParameterString(length,remediationString, langCode);
		
		return createRemediationParameter(rpVendorId, rpType, parameter);
	}
	
	public static PbMessageValueRemediationParameters createRemediationParameterUri(final long rpVendorId, final long rpType, String uri){
		
		if(uri == null){
			throw new NullPointerException("URI cannot be null.");
		}
		if(rpVendorId != IETFConstants.IETF_PEN_VENDORID || rpType != PbMessageRemediationParameterTypeEnum.IETF_URI.type()){
			throw new IllegalArgumentException("Requested remediation value is not supported in message with remediation vendor ID "+ rpVendorId +" and of remediation type "+ rpType +".");
		}
		
		URI temp = URI.create(uri);
		
		PbMessageValueRemediationParameterUri parameter = new PbMessageValueRemediationParameterUri(temp.toString().getBytes().length,temp);
		
		return createRemediationParameter(rpVendorId, rpType, parameter);
		
	}
	
	private static PbMessageValueRemediationParameters createRemediationParameter(final long rpVendorId, final long rpType, final AbstractPbMessageSubValue parameter){
		
		if(rpVendorId > IETFConstants.IETF_MAX_VENDOR_ID){
			throw new IllegalArgumentException("Vendor ID is greater than "+ Long.toString(IETFConstants.IETF_MAX_VENDOR_ID) + ".");
		}
		if(rpType > IETFConstants.IETF_MAX_TYPE){
			throw new IllegalArgumentException("Type is greater than "+ Long.toString(IETFConstants.IETF_MAX_TYPE) + ".");
		}

		long length = PbMessageTlvFixedLength.REM_PAR_VALUE.length() + parameter.getLength();
		
		return new PbMessageValueRemediationParameters(rpVendorId, rpType, length,parameter);
	}
	
}
