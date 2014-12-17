package org.ietf.nea.pb.message.util;

import java.nio.charset.Charset;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLengthEnum;
import org.ietf.nea.pb.validate.rules.LangCodeStringLimit;
import org.ietf.nea.pb.validate.rules.NoNullTerminatedString;
import org.ietf.nea.pb.validate.rules.NoZeroString;

public class PbMessageValueRemediationParameterStringBuilderIetf implements PbMessageValueRemediationParameterStringBuilder{

	private long length;
    private String remediationString;   // variable length, UTF-8 encoded, NUL termination MUST NOT be included.
    private String langCode;            // variable length, US-ASCII string composed of a well-formed RFC 4646 [3] language tag
    
    public PbMessageValueRemediationParameterStringBuilderIetf(){
    	this.length = PbMessageTlvFixedLengthEnum.REM_STR_SUB_VALUE.length();
    	this.remediationString = "";
    	this.langCode = "";
    }

	@Override
	public PbMessageValueRemediationParameterStringBuilder setRemediationString(String remediationString) throws RuleException {

		NoZeroString.check(remediationString);
		NoNullTerminatedString.check(remediationString);
		this.remediationString = remediationString;
		this.updateLength();
		
		return this;
	}

	@Override
	public PbMessageValueRemediationParameterStringBuilder setLangCode(String langCode) throws RuleException {
		
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
	public PbMessageValueRemediationParameterString toObject() throws RuleException {

		// check again because it has to set properly
		NoZeroString.check(this.remediationString);
		
		return new PbMessageValueRemediationParameterString(this.length, this.remediationString, this.langCode);
	}

	@Override
	public PbMessageValueRemediationParameterStringBuilder newInstance() {

		return new PbMessageValueRemediationParameterStringBuilderIetf();
	}

	private void updateLength(){
		this.length = PbMessageTlvFixedLengthEnum.REM_STR_SUB_VALUE.length();
		if(remediationString.length() > 0){
			this.length += remediationString.getBytes(Charset.forName("UTF-8")).length;
		}
		if(langCode.length() > 0){
			this.length += langCode.getBytes(Charset.forName("US-ASCII")).length;
		}
	}
}
