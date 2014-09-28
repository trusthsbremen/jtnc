package org.ietf.nea.pb.message;

import org.ietf.nea.pb.validate.rules.LangCodeStringLimit;
import org.ietf.nea.pb.validate.rules.NoNullTerminatedString;
import org.ietf.nea.pb.validate.rules.NoZeroString;

import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;

public class PbMessageValueRemediationParameterStringBuilderIetf implements PbMessageValueRemediationParameterStringBuilder{

    private String remediationString;   // variable length, UTF-8 encoded, NUL termination MUST NOT be included.
    private String langCode;            // variable length, US-ASCII string composed of a well-formed RFC 4646 [3] language tag
    
    public PbMessageValueRemediationParameterStringBuilderIetf(){
    	this.remediationString = "";
    	this.langCode = "";
    }

	/* (non-Javadoc)
	 * @see org.ietf.nea.pb.message.PbMessageValueRmediationParameterStringBuilder#setRemediationString(java.lang.String)
	 */
	@Override
	public PbMessageValueRemediationParameterStringBuilder setRemediationString(String remediationString) throws ValidationException {

		NoZeroString.check(remediationString);
		NoNullTerminatedString.check(remediationString);
		this.remediationString = remediationString;

		return this;
	}

	/* (non-Javadoc)
	 * @see org.ietf.nea.pb.message.PbMessageValueRmediationParameterStringBuilder#setLangCode(java.lang.String)
	 */
	@Override
	public PbMessageValueRemediationParameterStringBuilder setLangCode(String langCode) throws ValidationException {
		
		// Zero length string for language code allowed.
        if(langCode != null){
        	NoNullTerminatedString.check(langCode);
        	LangCodeStringLimit.check(langCode);
        	this.langCode = langCode;
        }
		
        return this;
	}

	@Override
	public PbMessageValueRemediationParameterString toValue() throws ValidationException {

		// check again because it has to set properly
		NoZeroString.check(this.remediationString);
		
		return new PbMessageValueRemediationParameterString(this.remediationString, this.langCode);
	}

	@Override
	public PbMessageValueRemediationParameterStringBuilder clear() {

		return new PbMessageValueRemediationParameterStringBuilderIetf();
	}

}
