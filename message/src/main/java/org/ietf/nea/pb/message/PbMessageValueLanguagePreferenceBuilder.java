package org.ietf.nea.pb.message;

import org.ietf.nea.exception.RuleException;

import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageValueBuilder;

public interface PbMessageValueLanguagePreferenceBuilder extends TnccsMessageValueBuilder{

	public abstract PbMessageValueLanguagePreferenceBuilder setLanguagePreference(String languagePreference)
			throws RuleException;

}