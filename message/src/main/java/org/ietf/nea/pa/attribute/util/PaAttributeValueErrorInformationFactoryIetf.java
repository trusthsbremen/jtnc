package org.ietf.nea.pa.attribute.util;

import org.ietf.nea.pa.attribute.PaAttributeHeader;
import org.ietf.nea.pa.attribute.enums.PaAttributeErrorCodeEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLength;
import org.ietf.nea.pa.message.PaMessageHeader;

import de.hsbremen.tc.tnc.IETFConstants;

public class PaAttributeValueErrorInformationFactoryIetf {
	
	public static PaAttributeValueErrorInformationInvalidParam createErrorInformationInvalidParameter(long errorVendorId, long errorCode, PaMessageHeader messageHeader, long offset){
		
		if(messageHeader == null){
			throw new NullPointerException("Message header cannot be null.");
		}
		
		if(offset < 0){
			throw new IllegalArgumentException("Offset cannot be negative.");
		}
		
		if(errorVendorId != IETFConstants.IETF_PEN_VENDORID || errorCode != PaAttributeErrorCodeEnum.IETF_INVALID_PARAMETER.code()){
			throw new IllegalArgumentException("Requested error value is not supported in attribute with error vendor ID "+ errorVendorId +" and code "+ errorCode +".");
		}
		
		long length = PaAttributeTlvFixedLength.MESSAGE.length() + 4; // 4 = offset length
		
		return new PaAttributeValueErrorInformationInvalidParam(length, messageHeader, offset);
	}
	
	public static PaAttributeValueErrorInformationUnsupportedAttribute createErrorInformationUnsupportedAttribute(long errorVendorId, long errorCode, PaMessageHeader messageHeader, PaAttributeHeader attributeHeader){
		
		if(messageHeader == null){
			throw new NullPointerException("Message header cannot be null.");
		}
		
		if(attributeHeader == null){
			throw new NullPointerException("Attribute header cannot be null.");
		}
		
		if(errorVendorId != IETFConstants.IETF_PEN_VENDORID || errorCode != PaAttributeErrorCodeEnum.IETF_UNSUPPORTED_MANDATORY_ATTRIBUTE.code()){
			throw new IllegalArgumentException("Requested error value is not supported in attribute with error vendor ID "+ errorVendorId +" and code "+ errorCode +".");
		}
		
		long length = PaAttributeTlvFixedLength.MESSAGE.length() + PaAttributeTlvFixedLength.ATTRIBUTE.length() - 4; // - 4 = attribute length is ignored
		
		return new PaAttributeValueErrorInformationUnsupportedAttribute(length, messageHeader, attributeHeader);
	}
	
	public static PaAttributeValueErrorInformationUnsupportedVersion createErrorInformationUnsupportedVersion(long errorVendorId, long errorCode, PaMessageHeader messageHeader, short maxVersion, short minVersion){
		
		if(messageHeader == null){
			throw new NullPointerException("Message header cannot be null.");
		}

		if(errorVendorId != IETFConstants.IETF_PEN_VENDORID || errorCode != PaAttributeErrorCodeEnum.IETF_UNSUPPORTED_VERSION.code()){
			throw new IllegalArgumentException("Requested error value is not supported in attribute with error vendor ID "+ errorVendorId +" and code "+ errorCode +".");
		}
		
		long length = PaAttributeTlvFixedLength.MESSAGE.length() + 4; // 4 = min+max version length
		
		return new PaAttributeValueErrorInformationUnsupportedVersion(length, messageHeader, maxVersion, minVersion);
	}
}
