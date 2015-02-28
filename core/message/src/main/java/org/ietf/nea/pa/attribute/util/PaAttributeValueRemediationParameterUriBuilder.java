package org.ietf.nea.pa.attribute.util;

import de.hsbremen.tc.tnc.message.exception.RuleException;

public interface PaAttributeValueRemediationParameterUriBuilder extends PaAttributeValueRemediationParameterBuilder{

	/**
	 * @param uri the uri to set
	 * @throws RuleException 
	 */
	public abstract  PaAttributeValueRemediationParameterUriBuilder setUri(String uri) throws RuleException;

}