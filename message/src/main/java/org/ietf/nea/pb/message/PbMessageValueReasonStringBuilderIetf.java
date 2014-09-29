package org.ietf.nea.pb.message;

import java.nio.charset.Charset;

import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLength;
import org.ietf.nea.pb.validate.rules.LangCodeStringLimit;
import org.ietf.nea.pb.validate.rules.NoNullTerminatedString;
import org.ietf.nea.pb.validate.rules.NoZeroString;

import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;

public class PbMessageValueReasonStringBuilderIetf implements PbMessageValueReasonStringBuilder{

	private long length;
    private String reasonString;        // variable length, UTF-8 encoded, NUL termination MUST NOT be included.
    private String langCode;            // variable length, US-ASCII string composed of a well-formed RFC 4646 [3] language tag
    
    public PbMessageValueReasonStringBuilderIetf(){
    	this.length = PbMessageTlvFixedLength.REA_STR_VALUE.length();
    	this.reasonString = "";
    	this.langCode = "";
    }

	/* (non-Javadoc)
	 * @see org.ietf.nea.pb.message.PbMessageValueReasonStringBuilder#setReasonString(java.lang.String)
	 */
	@Override
	public PbMessageValueReasonStringBuilder setReasonString(String reasonString) throws ValidationException {

		NoZeroString.check(reasonString);
		NoNullTerminatedString.check(reasonString);
		this.reasonString = reasonString;
		this.updateLength();
		
		return this;
	}

	/* (non-Javadoc)
	 * @see org.ietf.nea.pb.message.PbMessageValueReasonStringBuilder#setLangCode(java.lang.String)
	 */
	@Override
	public PbMessageValueReasonStringBuilder setLangCode(String langCode) throws ValidationException {
		
		// Zero length string for language code allowed.
        if(langCode != null){
        	NoNullTerminatedString.check(langCode);
        	LangCodeStringLimit.check(langCode);
        	this.langCode = langCode;
        	this.updateLength();
        }
        
        return this;
	}

	@Override
	public PbMessageValueReasonString toValue() throws ValidationException {

		// check again because it has to set properly
		NoZeroString.check(this.reasonString);
		
		return new PbMessageValueReasonString(this.length, this.reasonString, this.langCode);
	}

	@Override
	public PbMessageValueReasonStringBuilder clear() {

		return new PbMessageValueReasonStringBuilderIetf();
	}

	private void updateLength(){
		this.length = PbMessageTlvFixedLength.REA_STR_VALUE.length();
		if(reasonString.length() > 0){
			this.length += reasonString.getBytes(Charset.forName("UTF-8")).length;
		}
		if(langCode.length() > 0){
			this.length += langCode.getBytes(Charset.forName("US-ASCII")).length;
		}
	}
}
