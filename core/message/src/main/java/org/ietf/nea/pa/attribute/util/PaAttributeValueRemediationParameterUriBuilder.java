package org.ietf.nea.pa.attribute.util;

import org.ietf.nea.exception.RuleException;

public interface PaAttributeValueRemediationParameterUriBuilder extends PaAttributeValueRemediationParameterBuilder{

	/**
	 * @param uri the uri to set
	 * @throws RuleException 
	 */
	public abstract  PaAttributeValueRemediationParameterUriBuilder setUri(String uri) throws RuleException;

}