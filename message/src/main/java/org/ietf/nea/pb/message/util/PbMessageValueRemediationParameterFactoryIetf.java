package org.ietf.nea.pb.message.util;

import java.net.URI;
import java.nio.charset.Charset;

import org.ietf.nea.pb.message.enums.PbMessageRemediationParameterTypeEnum;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLength;

import de.hsbremen.tc.tnc.IETFConstants;

public class PbMessageValueRemediationParameterFactoryIetf {
	
	
	public static PbMessageValueRemediationParameterString createRemediationParameterString(final long rpVendorId, final long rpType, final String remediationString, final String langCode){
	
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
		
		return parameter;
	}
	
	public static PbMessageValueRemediationParameterUri createRemediationParameterUri(final long rpVendorId, final long rpType, String uri){
		
		if(uri == null){
			throw new NullPointerException("URI cannot be null.");
		}
		if(rpVendorId != IETFConstants.IETF_PEN_VENDORID || rpType != PbMessageRemediationParameterTypeEnum.IETF_URI.type()){
			throw new IllegalArgumentException("Requested remediation value is not supported in message with remediation vendor ID "+ rpVendorId +" and of remediation type "+ rpType +".");
		}
		
		URI temp = URI.create(uri);
		
		PbMessageValueRemediationParameterUri parameter = new PbMessageValueRemediationParameterUri(temp.toString().getBytes().length,temp);

		return parameter;
	}
	
	
	
}
