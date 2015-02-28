package org.ietf.nea.pa.attribute.util;

import de.hsbremen.tc.tnc.message.exception.RuleException;

public interface PaAttributeValueRemediationParameterBuilder {

	public abstract AbstractPaAttributeValueRemediationParameter toObject() throws RuleException;
	
	public abstract PaAttributeValueRemediationParameterBuilder newInstance();
}
