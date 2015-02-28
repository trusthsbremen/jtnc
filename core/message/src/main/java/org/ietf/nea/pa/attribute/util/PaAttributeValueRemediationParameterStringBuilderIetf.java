package org.ietf.nea.pa.attribute.util;

import java.nio.charset.Charset;

import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;
import org.ietf.nea.pa.validate.rules.NoNullTerminatedString;
import org.ietf.nea.pa.validate.rules.NoZeroString;
import org.ietf.nea.pa.validate.rules.StringLengthLimit;

import de.hsbremen.tc.tnc.message.exception.RuleException;

public class PaAttributeValueRemediationParameterStringBuilderIetf implements PaAttributeValueRemediationParameterStringBuilder{

	private long length;
    private String remediationString;   // variable length, UTF-8 encoded, NUL termination MUST NOT be included.
    private String langCode;            // variable length, US-ASCII string composed of a well-formed RFC 4646 [3] language tag
    
    public PaAttributeValueRemediationParameterStringBuilderIetf(){
    	this.length = PaAttributeTlvFixedLengthEnum.REM_PAR_STR.length();
    	this.remediationString = "";
    	this.langCode = "";
    }

	/* (non-Javadoc)
	 * @see org.ietf.nea.pb.message.PbMessageValueRmediationParameterStringBuilder#setRemediationString(java.lang.String)
	 */
	@Override
	public PaAttributeValueRemediationParameterStringBuilder setRemediationString(String remediationString) throws RuleException {

		NoZeroString.check(remediationString);
		NoNullTerminatedString.check(remediationString);
		this.remediationString = remediationString;
		this.updateLength();
		
		return this;
	}

	/* (non-Javadoc)
	 * @see org.ietf.nea.pb.message.PbMessageValueRmediationParameterStringBuilder#setLangCode(java.lang.String)
	 */
	@Override
	public PaAttributeValueRemediationParameterStringBuilder setLangCode(String langCode) throws RuleException {
		
		// Zero length string for language code allowed.
        if(langCode != null){
        	NoNullTerminatedString.check(langCode);
        	StringLengthLimit.check(langCode, 0xFF);
        	this.langCode = langCode;
        	this.updateLength();
        }
		
        return this;
	}

	@Override
	public PaAttributeValueRemediationParameterString toObject() throws RuleException {

		// check again because it has to set properly
		NoZeroString.check(this.remediationString);
		
		return new PaAttributeValueRemediationParameterString(this.length, this.remediationString, this.langCode);
	}

	@Override
	public PaAttributeValueRemediationParameterStringBuilder newInstance() {

		return new PaAttributeValueRemediationParameterStringBuilderIetf();
	}

	private void updateLength(){
		this.length = PaAttributeTlvFixedLengthEnum.REM_PAR_STR.length();
		if(remediationString.length() > 0){
			this.length += remediationString.getBytes(Charset.forName("UTF-8")).length;
		}
		if(langCode.length() > 0){
			this.length += langCode.getBytes(Charset.forName("US-ASCII")).length;
		}
	}
}
