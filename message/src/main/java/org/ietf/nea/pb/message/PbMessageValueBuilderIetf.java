package org.ietf.nea.pb.message;

import java.nio.charset.Charset;

import org.ietf.nea.pb.message.enums.PbMessageAccessRecommendationEnum;
import org.ietf.nea.pb.message.enums.PbMessageAssessmentResultEnum;
import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.message.enums.PbMessageErrorFlagsEnum;
import org.ietf.nea.pb.message.enums.PbMessageImFlagsEnum;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLengthEnum;
import org.ietf.nea.pb.message.util.AbstractPbMessageValueErrorParameter;
import org.ietf.nea.pb.message.util.AbstractPbMessageValueRemediationParameter;
import org.ietf.nea.pb.message.util.PbMessageValueErrorParameterFactoryIetf;
import org.ietf.nea.pb.message.util.PbMessageValueErrorParameterOffset;
import org.ietf.nea.pb.message.util.PbMessageValueErrorParameterVersion;
import org.ietf.nea.pb.message.util.PbMessageValueRemediationParameterFactoryIetf;
import org.ietf.nea.pb.message.util.PbMessageValueRemediationParameterString;
import org.ietf.nea.pb.message.util.PbMessageValueRemediationParameterUri;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.util.NotNull;

public class PbMessageValueBuilderIetf {
	
	public static PbMessageValueIm createImValue(final PbMessageImFlagsEnum[] imFlags, final long subVendorId, final long subType, final int collectorId, final int validatorId, final byte[] message){

	    NotNull.check("Error flags cannot be null.", (Object) imFlags);
		byte[] imMessage = (message != null) ? message : new byte[0];
		
		if(subVendorId > IETFConstants.IETF_MAX_VENDOR_ID){
			throw new IllegalArgumentException("Vendor ID is greater than "+ Long.toString(IETFConstants.IETF_MAX_VENDOR_ID) + ".");
		}
		if(subType > IETFConstants.IETF_MAX_TYPE){
			throw new IllegalArgumentException("Type is greater than "+ Long.toString(IETFConstants.IETF_MAX_TYPE) + ".");
		}

		long length = PbMessageTlvFixedLengthEnum.IM_VALUE.length() + imMessage.length;
		
		return new PbMessageValueIm(imFlags, subVendorId, subType, collectorId, validatorId, length, imMessage);
	}
	
	public static PbMessageValueAccessRecommendation createAccessRecommendationValue(final PbMessageAccessRecommendationEnum recommendation){
		
		NotNull.check("Recommendation cannot be null.", recommendation);
		
		return new PbMessageValueAccessRecommendation(PbMessageTlvFixedLengthEnum.ACC_REC_VALUE.length(),recommendation);
	}
	
	public static PbMessageValueAssessmentResult createAssessmentResultValue(final PbMessageAssessmentResultEnum result){
		
		NotNull.check("Result cannot be null.", result);
		
		return new PbMessageValueAssessmentResult(PbMessageTlvFixedLengthEnum.ASS_RES_VALUE.length(),result);
	}
	
	public static PbMessageValueError createErrorValueWithOffset(final PbMessageErrorFlagsEnum[] errorFlags, final long errorVendorId, final int errorCode, final long offset){
	    NotNull.check("Error flags cannot be null.", (Object) errorFlags);
	    
		PbMessageValueErrorParameterOffset errorParameter = PbMessageValueErrorParameterFactoryIetf.createErrorParameterOffset(errorVendorId, errorCode, offset);
		
		return createError(errorFlags, errorVendorId, errorCode, errorParameter);
		
	}
	
	public static PbMessageValueError createErrorValueWithVersion(final PbMessageErrorFlagsEnum[] errorFlags, final long errorVendorId, final int errorCode, final short badVersion, final short maxVersion, final short minVersion){
	    NotNull.check("Error flags cannot be null.", (Object) errorFlags);
	    
		PbMessageValueErrorParameterVersion errorParameter = PbMessageValueErrorParameterFactoryIetf.createErrorParameterVersion(errorVendorId, errorCode, badVersion, maxVersion, minVersion);
		
		return createError(errorFlags, errorVendorId, errorCode, errorParameter);
		
	}
	
	public static PbMessageValueError createErrorValueSimple(final PbMessageErrorFlagsEnum[] errorFlags, final long errorVendorId, final int errorCode){
	    NotNull.check("Error flags cannot be null.", (Object) errorFlags);

		if(errorVendorId == IETFConstants.IETF_PEN_VENDORID && (errorCode == PbMessageErrorCodeEnum.IETF_LOCAL.code() || errorCode == PbMessageErrorCodeEnum.IETF_UNEXPECTED_BATCH_TYPE.code() )){
			return createError(errorFlags, errorVendorId, errorCode, null);
		}
		
		throw new IllegalArgumentException("Error parameter cannot be null for error with vendor ID " + errorVendorId + " and with code " + errorCode + ".");
	}
	
	public static PbMessageValueLanguagePreference createLanguagePreferenceValue(final String preferedLanguage){
		
		NotNull.check("Result cannot be null.", preferedLanguage);
		
		long length = preferedLanguage.getBytes(Charset.forName("US-ASCII")).length;
		
		return new PbMessageValueLanguagePreference(length,preferedLanguage);
		
	}
	
	public static PbMessageValueExperimental createExperimentalValue(final String message){
		
		NotNull.check("Result cannot be null.", message);
		
		return new PbMessageValueExperimental(message.length(),message);
	}
	
	public static PbMessageValueReasonString createReasonStringValue(final String reasonString, final String langCode){
		
		NotNull.check("Reason string cannot be null.", reasonString);
		NotNull.check("Language code cannot be null.", langCode);
		if(langCode.length() > 0xFF){
			throw new IllegalArgumentException("Language code length " +langCode.length()+ "is to long.");
		}

		long length = PbMessageTlvFixedLengthEnum.REA_STR_VALUE.length();
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

		long length = PbMessageTlvFixedLengthEnum.REM_PAR_VALUE.length() + parameter.getLength();
		
		return new PbMessageValueRemediationParameters(rpVendorId, rpType, length,parameter);
	}
	
	private static PbMessageValueError createError(final PbMessageErrorFlagsEnum[] flags, final long errorVendorId, final int errorCode, final AbstractPbMessageValueErrorParameter errorParameter){

		if(errorVendorId > IETFConstants.IETF_MAX_VENDOR_ID){
			throw new IllegalArgumentException("Vendor ID is greater than "+ Long.toString(IETFConstants.IETF_MAX_VENDOR_ID) + ".");
		}
		if(errorCode > IETFConstants.IETF_MAX_ERROR_CODE){
			throw new IllegalArgumentException("Code is greater than "+ Long.toString(IETFConstants.IETF_MAX_ERROR_CODE) + ".");
		}

		long length = PbMessageTlvFixedLengthEnum.ERR_VALUE.length() + ((errorParameter != null)?errorParameter.getLength():0);
		
		return new PbMessageValueError(flags, errorVendorId, errorCode, length, errorParameter);
	}
	
}
