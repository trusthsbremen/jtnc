package org.ietf.nea.pb.message.util;

import org.ietf.nea.exception.RuleException;

public interface PbMessageValueRemediationParameterStringBuilder extends PbMessageValueRemediationParameterBuilder{

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