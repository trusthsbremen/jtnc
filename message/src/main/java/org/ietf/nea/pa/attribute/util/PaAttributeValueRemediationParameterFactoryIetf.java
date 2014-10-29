package org.ietf.nea.pa.attribute.util;

import java.net.URI;
import java.nio.charset.Charset;

import org.ietf.nea.pa.attribute.enums.PaAttributeRemediationParameterTypeEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLength;

import de.hsbremen.tc.tnc.IETFConstants;

public class PaAttributeValueRemediationParameterFactoryIetf {
	
	
	public static PaAttributeValueRemediationParameterString createRemediationParameterString(final long rpVendorId, final long rpType, final String remediationString, final String langCode){
	
		if(remediationString == null){
			throw new NullPointerException("Remediation string cannot be null.");
		}
		if(langCode == null){
			throw new NullPointerException("Language code cannot be null.");
		}
		if(langCode.length() > 0xFF){
			throw new IllegalArgumentException("Language code length " +langCode.length()+ "is to long.");
		}
		if(rpVendorId != IETFConstants.IETF_PEN_VENDORID || rpType != PaAttributeRemediationParameterTypeEnum.IETF_STRING.type()){
			throw new IllegalArgumentException("Requested remediation value is not supported in attribute with remediation vendor ID "+ rpVendorId +" and of remediation type "+ rpType +".");
		}
		
		long length = PaAttributeTlvFixedLength.REM_PAR_STR.length();
		if(remediationString.length() > 0){
			length += remediationString.getBytes(Charset.forName("UTF-8")).length;
		}
		if(langCode.length() > 0){
			length += langCode.getBytes(Charset.forName("US-ASCII")).length;
		}
		
		PaAttributeValueRemediationParameterString parameter = new PaAttributeValueRemediationParameterString(length, remediationString, langCode);
		
		return parameter;
	}
	
	public static PaAttributeValueRemediationParameterUri createRemediationParameterUri(final long rpVendorId, final long rpType, String uri){
		
		if(uri == null){
			throw new NullPointerException("URI cannot be null.");
		}
		if(rpVendorId != IETFConstants.IETF_PEN_VENDORID || rpType != PaAttributeRemediationParameterTypeEnum.IETF_URI.type()){
			throw new IllegalArgumentException("Requested remediation value is not supported in attribute with remediation vendor ID "+ rpVendorId +" and of remediation type "+ rpType +".");
		}
		
		URI temp = URI.create(uri);
		
		PaAttributeValueRemediationParameterUri parameter = new PaAttributeValueRemediationParameterUri(temp.toString().getBytes().length,temp);

		return parameter;
	}
	
	
	
}
