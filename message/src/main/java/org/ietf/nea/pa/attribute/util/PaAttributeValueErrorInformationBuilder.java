package org.ietf.nea.pa.attribute.util;

import org.ietf.nea.exception.RuleException;

public interface PaAttributeValueErrorInformationBuilder {

	public abstract AbstractPaAttributeValueErrorInformation toObject() throws RuleException;
	
	public abstract PaAttributeValueErrorInformationBuilder newInstance();
}
