package org.ietf.nea.pb.message;

import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageSubValueBuilder;

public interface PbMessageValueRemediationParameterStringBuilder extends TnccsMessageSubValueBuilder{

	/**
	 * @param remediationString the remediationString to set
	 * @throws ValidationException 
	 */
	public abstract void setRemediationString(String remediationString)
			throws ValidationException;

	/**
	 * @param langCode the langCode to set
	 * @throws ValidationException 
	 */
	public abstract void setLangCode(String langCode)
			throws ValidationException;

}