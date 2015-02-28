package org.ietf.nea.pa.attribute.util;

import java.util.Arrays;

import org.ietf.nea.pa.attribute.PaAttributeHeader;
import org.ietf.nea.pa.attribute.enums.PaAttributeErrorCodeEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.util.NotNull;

public class PaAttributeValueErrorInformationFactoryIetf {
	
	public static PaAttributeValueErrorInformationInvalidParam createErrorInformationInvalidParameter(long errorVendorId, long errorCode, byte[] messageHeader, long offset){
		
	    NotNull.check("Message header cannot be null.", messageHeader);
		
		if(offset < 0){
			throw new IllegalArgumentException("Offset cannot be negative.");
		}
		
		if(errorVendorId != IETFConstants.IETF_PEN_VENDORID || errorCode != PaAttributeErrorCodeEnum.IETF_INVALID_PARAMETER.code()){
			throw new IllegalArgumentException("Requested error value is not supported in attribute with error vendor ID "+ errorVendorId +" and code "+ errorCode +".");
		}
		
		RawMessageHeader header = parseHeader(messageHeader);
		
		long length = PaAttributeTlvFixedLengthEnum.MESSAGE.length() + 4; // 4 = offset length
		
		return new PaAttributeValueErrorInformationInvalidParam(length, header, offset);
	}

	public static PaAttributeValueErrorInformationUnsupportedAttribute createErrorInformationUnsupportedAttribute(long errorVendorId, long errorCode, byte[] messageHeader, PaAttributeHeader attributeHeader){
		
	    NotNull.check("Message header cannot be null.", messageHeader);
	    NotNull.check("Attribute header cannot be null.", attributeHeader);
		
		
		if(errorVendorId != IETFConstants.IETF_PEN_VENDORID || errorCode != PaAttributeErrorCodeEnum.IETF_UNSUPPORTED_MANDATORY_ATTRIBUTE.code()){
			throw new IllegalArgumentException("Requested error value is not supported in attribute with error vendor ID "+ errorVendorId +" and code "+ errorCode +".");
		}
		
		RawMessageHeader header = parseHeader(messageHeader);
		
		long length = PaAttributeTlvFixedLengthEnum.MESSAGE.length() + PaAttributeTlvFixedLengthEnum.ATTRIBUTE.length() - 4; // - 4 = attribute length is ignored
		
		return new PaAttributeValueErrorInformationUnsupportedAttribute(length, header, attributeHeader);
	}
	
	public static PaAttributeValueErrorInformationUnsupportedVersion createErrorInformationUnsupportedVersion(long errorVendorId, long errorCode, byte[] messageHeader, short maxVersion, short minVersion){
  
	    NotNull.check("Message header cannot be null.", messageHeader);

		if(errorVendorId != IETFConstants.IETF_PEN_VENDORID || errorCode != PaAttributeErrorCodeEnum.IETF_UNSUPPORTED_VERSION.code()){
			throw new IllegalArgumentException("Requested error value is not supported in attribute with error vendor ID "+ errorVendorId +" and code "+ errorCode +".");
		}
		
		RawMessageHeader header = parseHeader(messageHeader);
		
		long length = PaAttributeTlvFixedLengthEnum.MESSAGE.length() + 4; // 4 = min+max version length
		
		return new PaAttributeValueErrorInformationUnsupportedVersion(length, header, maxVersion, minVersion);
	}
	
	private static RawMessageHeader parseHeader(byte[] messageHeader) {
		
		byte[] sizedMessageHeader = Arrays.copyOf(messageHeader, PaAttributeTlvFixedLengthEnum.MESSAGE.length());
		
		short version = sizedMessageHeader[0];
		byte[] reserved = Arrays.copyOfRange(sizedMessageHeader, 1,4);
		
		long value = 0L;
		byte[] b = Arrays.copyOfRange(sizedMessageHeader, 4,8);
		for (int i = 0; i < b.length; i++) {
	        value = (value << 8) + (b[i] & 0xFF);
	    }
		long identifier = value;
		
	    return new RawMessageHeader(version, reserved, identifier);
	}
}
