package org.ietf.nea.pb.message;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessageValueBuilder;

public interface PbMessageValueLanguagePreferenceBuilder extends TnccsMessageValueBuilder{

	public abstract PbMessageValueLanguagePreferenceBuilder setLanguagePreference(String languagePreference)
			throws RuleException;

}