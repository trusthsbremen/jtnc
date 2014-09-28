package org.ietf.nea.pb.message;

import org.ietf.nea.pb.validate.rules.LangCodeStringLimit;
import org.ietf.nea.pb.validate.rules.NoNullTerminatedString;
import org.ietf.nea.pb.validate.rules.NoZeroString;

import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageSubValue;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageSubValueBuilder;

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
	public void setRemediationString(String remediationString) throws ValidationException {

		NoZeroString.check(remediationString);
		NoNullTerminatedString.check(remediationString);
		this.remediationString = remediationString;

	}

	/* (non-Javadoc)
	 * @see org.ietf.nea.pb.message.PbMessageValueRmediationParameterStringBuilder#setLangCode(java.lang.String)
	 */
	@Override
	public void setLangCode(String langCode) throws ValidationException {
		 // Zero length string for language code allowed.
        if(langCode != null){
        	NoNullTerminatedString.check(langCode);
        	LangCodeStringLimit.check(langCode);
        	this.langCode = langCode;
        }
		
	}

	@Override
	public TnccsMessageSubValue toValue() throws ValidationException {

		// check again because it has to set properly
		NoZeroString.check(this.remediationString);
		
		return new PbMessageValueRemediationParameterString(this.remediationString, this.langCode);
	}

	@Override
	public TnccsMessageSubValueBuilder clear() {

		return new PbMessageValueRemediationParameterStringBuilderIetf();
	}

}
