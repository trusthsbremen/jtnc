package org.ietf.nea.pa.attribute.util;

import de.hsbremen.tc.tnc.message.exception.RuleException;

public interface PaAttributeValueErrorInformationBuilder {

	public abstract AbstractPaAttributeValueErrorInformation toObject() throws RuleException;
	
	public abstract PaAttributeValueErrorInformationBuilder newInstance();
}
