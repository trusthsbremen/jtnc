package org.ietf.nea.pb.message.util;

import java.nio.charset.Charset;

public class PbMessageValueRemediationParameterString extends AbstractPbMessageValueRemediationParameter{
  
    private final long stringLength;          // 32 bit(s) length of the string in octets
    private final String remediationString;   // variable length, UTF-8 encoded, NUL termination MUST NOT be included.
    private final byte langCodeLength;        // 8 bit(s) length of language code in octets, 0 = language unknown
    private final String langCode;            // variable length, US-ASCII string composed of a well-formed RFC 4646 [3] language tag
    
//    //overload the superclass default vendorId, type and length value
//    protected final long rpVendorId = IETFConstants.IETF_PEN_VENDORID; // 24 bit(s)
//    protected final long rpType = PbMessageRemediationParameterTypeEnum.IETF_URI.type();         // 32 bit(s)

    PbMessageValueRemediationParameterString(final long length,final String remediationString, final String langCode){
    	super(length);
		this.remediationString = remediationString;
		this.stringLength = remediationString.getBytes(Charset.forName("UTF-8")).length;
		this.langCode = langCode;
		this.langCodeLength = (byte)langCode.getBytes(Charset.forName("US-ASCII")).length;
    }

	/**
	 * @return the stringLength
	 */
	public long getStringLength() {
		return this.stringLength;
	}

	/**
	 * @return the remediationString
	 */
	public String getRemediationString() {
		return this.remediationString;
	}

	/**
	 * @return the langCodeLength
	 */
	public byte getLangCodeLength() {
		return this.langCodeLength;
	}

	/**
	 * @return the langCode
	 */
	public String getLangCode() {
		return this.langCode;
	}
   
    
    
}
