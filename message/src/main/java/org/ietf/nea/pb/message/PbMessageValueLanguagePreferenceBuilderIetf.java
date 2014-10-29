package org.ietf.nea.pb.message;

import java.nio.charset.Charset;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pb.validate.rules.NoNullTerminatedString;

import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageValueBuilder;

public class PbMessageValueLanguagePreferenceBuilderIetf implements TnccsMessageValueBuilder, PbMessageValueLanguagePreferenceBuilder{

	private long length;
    private String languagePreference;  //32 bit(s), accept-Language header, as described in RFC 3282 [4]  as Accept-Language included in that RFC, US-ASCII only, no control characters allowed, no comments, no NUL termination)
     
    public PbMessageValueLanguagePreferenceBuilderIetf(){
    	this.length = 0;
    	this.languagePreference = "";
    }

	
	/* (non-Javadoc)
	 * @see org.ietf.nea.pb.message.PbMessageValueLanguagePreferenceBuilder#setReasonString(java.lang.String)
	 */
	@Override
	public PbMessageValueLanguagePreferenceBuilder setLanguagePreference(String languagePreference) throws RuleException {

		// TODO regular expression test for language string (RFC 2234).
		// No Null termination is one thing of that.
		NoNullTerminatedString.check(languagePreference);
		this.languagePreference = languagePreference;
		this.length = languagePreference.getBytes(Charset.forName("US-ASCII")).length;
		return this;
	}

	@Override
	public PbMessageValueLanguagePreference toValue(){

		return new PbMessageValueLanguagePreference(this.length,this.languagePreference);
	}

	@Override
	public PbMessageValueLanguagePreferenceBuilder clear() {

		return new PbMessageValueLanguagePreferenceBuilderIetf();
	}

}
