package org.ietf.nea.pa.attribute.util;

import org.ietf.nea.exception.RuleException;

public interface PaAttributeValueRemediationParameterStringBuilder extends PaAttributeValueRemediationParameterBuilder{

	/**
	 * @param remediationString the remediationString to set
	 * @throws RuleException 
	 */
	public abstract PaAttributeValueRemediationParameterStringBuilder setRemediationString(String remediationString)
			throws RuleException;

	/**
	 * @param langCode the langCode to set
	 * @throws RuleException 
	 */
	public abstract PaAttributeValueRemediationParameterStringBuilder setLangCode(String langCode)
			throws RuleException;
	
}