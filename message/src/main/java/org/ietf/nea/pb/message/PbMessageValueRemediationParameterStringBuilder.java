package org.ietf.nea.pb.message;

import org.ietf.nea.pb.exception.RuleException;

import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageSubValueBuilder;

public interface PbMessageValueRemediationParameterStringBuilder extends TnccsMessageSubValueBuilder{

	/**
	 * @param remediationString the remediationString to set
	 * @throws RuleException 
	 */
	public abstract PbMessageValueRemediationParameterStringBuilder setRemediationString(String remediationString)
			throws RuleException;

	/**
	 * @param langCode the langCode to set
	 * @throws RuleException 
	 */
	public abstract PbMessageValueRemediationParameterStringBuilder setLangCode(String langCode)
			throws RuleException;

}