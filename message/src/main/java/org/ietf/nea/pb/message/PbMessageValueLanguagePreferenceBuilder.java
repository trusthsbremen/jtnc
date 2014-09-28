package org.ietf.nea.pb.message;

import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageValueBuilder;

public interface PbMessageValueLanguagePreferenceBuilder extends TnccsMessageValueBuilder{

	public abstract void setLanguagePreference(String languagePreference)
			throws ValidationException;

}