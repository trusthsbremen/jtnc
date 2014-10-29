package org.ietf.nea.pb.message.util;

import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLength;

import de.hsbremen.tc.tnc.IETFConstants;

public class PbMessageValueErrorParameterFactoryIetf {
	
	
	public static PbMessageValueErrorParameterOffset createErrorParameterOffset(final long errorVendorId, final long errorType, final long offset){
	
		if(offset < 0){
			throw new IllegalArgumentException("Offset cannot be negative."); 
		}
		
		if(errorVendorId == IETFConstants.IETF_PEN_VENDORID && (errorType == PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code() || errorType == PbMessageErrorCodeEnum.IETF_UNSUPPORTED_MANDATORY_MESSAGE.code())){
			PbMessageValueErrorParameterOffset parameter = new PbMessageValueErrorParameterOffset(PbMessageTlvFixedLength.ERR_SUB_VALUE.length(),offset);
			return parameter;
		}
		
		throw new IllegalArgumentException("Requested error value is not supported in attribute with error vendor ID "+ errorVendorId +" and with error code "+ errorType +".");

	}
	
	public static PbMessageValueErrorParameterVersion createErrorParameterVersion(final long errorVendorId, final long errorType, short badVersion, short maxVersion, short minVersion){
		
		if(maxVersion < 0 || minVersion < 0){
			throw new IllegalArgumentException("Version information cannot be negative.");
		}
		
		if(errorVendorId == IETFConstants.IETF_PEN_VENDORID && errorType == PbMessageErrorCodeEnum.IETF_UNSUPPORTED_VERSION.code()){
			PbMessageValueErrorParameterVersion parameter = new PbMessageValueErrorParameterVersion(PbMessageTlvFixedLength.ERR_SUB_VALUE.length(), badVersion, maxVersion, minVersion);
			return parameter;
		}
		
		throw new IllegalArgumentException("Requested error value is not supported in attribute with error vendor ID "+ errorVendorId +" and with error code "+ errorType +".");
	}
	
	
	
}
