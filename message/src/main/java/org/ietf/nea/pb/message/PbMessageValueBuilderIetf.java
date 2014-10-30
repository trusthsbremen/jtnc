package org.ietf.nea.pb.message;

import java.nio.charset.Charset;

import org.ietf.nea.pb.message.enums.PbMessageAccessRecommendationEnum;
import org.ietf.nea.pb.message.enums.PbMessageAssessmentResultEnum;
import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.message.enums.PbMessageErrorFlagsEnum;
import org.ietf.nea.pb.message.enums.PbMessageImFlagsEnum;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLength;
import org.ietf.nea.pb.message.util.AbstractPbMessageValueErrorParameter;
import org.ietf.nea.pb.message.util.AbstractPbMessageValueRemediationParameter;
import org.ietf.nea.pb.message.util.PbMessageValueErrorParameterFactoryIetf;
import org.ietf.nea.pb.message.util.PbMessageValueErrorParameterOffset;
import org.ietf.nea.pb.message.util.PbMessageValueErrorParameterVersion;
import org.ietf.nea.pb.message.util.PbMessageValueRemediationParameterFactoryIetf;
import org.ietf.nea.pb.message.util.PbMessageValueRemediationParameterString;
import org.ietf.nea.pb.message.util.PbMessageValueRemediationParameterUri;

import de.hsbremen.tc.tnc.IETFConstants;

public class PbMessageValueBuilderIetf {
	
	public static PbMessageValueIm createImValue(final PbMessageImFlagsEnum[] imFlags, final long subVendorId, final long subType, final int collectorId, final int validatorId, final byte[] message){

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
	
	public static PbMessageValueError createErrorValueWithOffset(final PbMessageErrorFlagsEnum[] errorFlags, final long errorVendorId, final int errorCode, final long offset){
		
		PbMessageValueErrorParameterOffset errorParameter = PbMessageValueErrorParameterFactoryIetf.createErrorParameterOffset(errorVendorId, errorCode, offset);
		
		return createError(errorFlags, errorVendorId, errorCode, errorParameter);
		
	}
	
	public static PbMessageValueError createErrorValueWithVersion(final PbMessageErrorFlagsEnum[] errorFlags, final long errorVendorId, final int errorCode, final short badVersion, final short maxVersion, final short minVersion){
		
		PbMessageValueErrorParameterVersion errorParameter = PbMessageValueErrorParameterFactoryIetf.createErrorParameterVersion(errorVendorId, errorCode, badVersion, maxVersion, minVersion);
		
		return createError(errorFlags, errorVendorId, errorCode, errorParameter);
		
	}
	
	public static PbMessageValueError createErrorValueSimple(final PbMessageErrorFlagsEnum[] errorFlags, final long errorVendorId, final int errorCode){

		if(errorVendorId == IETFConstants.IETF_PEN_VENDORID && (errorCode == PbMessageErrorCodeEnum.IETF_LOCAL.code() || errorCode == PbMessageErrorCodeEnum.IETF_UNEXPECTED_BATCH_TYPE.code() )){
			return createError(errorFlags, errorVendorId, errorCode, null);
		}
		
		throw new NullPointerException("Error parameter cannot be NULL for error with vendor ID " + errorVendorId + " and with code " + errorCode + ".");
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
		
		PbMessageValueRemediationParameterString parameter = PbMessageValueRemediationParameterFactoryIetf.createRemediationParameterString(rpVendorId, rpType, remediationString, langCode);
		
		return createRemediationParameter(rpVendorId, rpType, parameter);
	}
	
	public static PbMessageValueRemediationParameters createRemediationParameterUri(final long rpVendorId, final long rpType, String uri){
		
		PbMessageValueRemediationParameterUri parameter = PbMessageValueRemediationParameterFactoryIetf.createRemediationParameterUri(rpVendorId, rpType, uri);
		
		return createRemediationParameter(rpVendorId, rpType, parameter);
		
	}
	
	private static PbMessageValueRemediationParameters createRemediationParameter(final long rpVendorId, final long rpType, final AbstractPbMessageValueRemediationParameter parameter){
		
		if(rpVendorId > IETFConstants.IETF_MAX_VENDOR_ID){
			throw new IllegalArgumentException("Vendor ID is greater than "+ Long.toString(IETFConstants.IETF_MAX_VENDOR_ID) + ".");
		}
		if(rpType > IETFConstants.IETF_MAX_TYPE){
			throw new IllegalArgumentException("Type is greater than "+ Long.toString(IETFConstants.IETF_MAX_TYPE) + ".");
		}

		long length = PbMessageTlvFixedLength.REM_PAR_VALUE.length() + parameter.getLength();
		
		return new PbMessageValueRemediationParameters(rpVendorId, rpType, length,parameter);
	}
	
	private static PbMessageValueError createError(final PbMessageErrorFlagsEnum[] flags, final long errorVendorId, final int errorCode, final AbstractPbMessageValueErrorParameter errorParameter){
		
		if(flags == null){
			throw new NullPointerException("Error flags cannot be null.");
		}
		
		if(errorVendorId > IETFConstants.IETF_MAX_VENDOR_ID){
			throw new IllegalArgumentException("Vendor ID is greater than "+ Long.toString(IETFConstants.IETF_MAX_VENDOR_ID) + ".");
		}
		if(errorCode > IETFConstants.IETF_MAX_ERROR_CODE){
			throw new IllegalArgumentException("Code is greater than "+ Long.toString(IETFConstants.IETF_MAX_ERROR_CODE) + ".");
		}

		long length = PbMessageTlvFixedLength.ERR_VALUE.length() + errorParameter.getLength();
		
		return new PbMessageValueError(flags, errorVendorId, errorCode, length, errorParameter);
	}
	
}
