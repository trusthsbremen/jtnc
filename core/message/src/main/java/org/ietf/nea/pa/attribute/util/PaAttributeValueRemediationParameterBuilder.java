package org.ietf.nea.pa.attribute.util;

import org.ietf.nea.exception.RuleException;

public interface PaAttributeValueRemediationParameterBuilder {

	public abstract AbstractPaAttributeValueRemediationParameter toObject() throws RuleException;
	
	public abstract PaAttributeValueRemediationParameterBuilder newInstance();
}
