package org.ietf.nea.pb.message;

import org.ietf.nea.pb.validate.rules.LangCodeStringLimit;
import org.ietf.nea.pb.validate.rules.NoNullTerminatedString;
import org.ietf.nea.pb.validate.rules.NoZeroString;

import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageValue;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageValueBuilder;

public class PbMessageValueReasonStringBuilderIetf implements PbMessageValueReasonStringBuilder{

    private String reasonString;        // variable length, UTF-8 encoded, NUL termination MUST NOT be included.
    private String langCode;            // variable length, US-ASCII string composed of a well-formed RFC 4646 [3] language tag
    
    public PbMessageValueReasonStringBuilderIetf(){
    	this.reasonString = "";
    	this.langCode = "";
    }

	/* (non-Javadoc)
	 * @see org.ietf.nea.pb.message.PbMessageValueReasonStringBuilder#setReasonString(java.lang.String)
	 */
	@Override
	public void setReasonString(String reasonString) throws ValidationException {

		NoZeroString.check(reasonString);
		NoNullTerminatedString.check(reasonString);
		this.reasonString = reasonString;

	}

	/* (non-Javadoc)
	 * @see org.ietf.nea.pb.message.PbMessageValueReasonStringBuilder#setLangCode(java.lang.String)
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
	public TnccsMessageValue toValue() throws ValidationException {

		// check again because it has to set properly
		NoZeroString.check(this.reasonString);
		
		return new PbMessageValueReasonString(this.reasonString, this.langCode);
	}

	@Override
	public TnccsMessageValueBuilder clear() {

		return new PbMessageValueReasonStringBuilderIetf();
	}

}
