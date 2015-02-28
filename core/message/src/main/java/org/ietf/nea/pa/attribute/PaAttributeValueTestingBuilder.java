package org.ietf.nea.pa.attribute;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.m.attribute.ImAttributeValueBuilder;

public interface PaAttributeValueTestingBuilder extends ImAttributeValueBuilder{

	public abstract PaAttributeValueTestingBuilder setContent(String Content) throws RuleException;

}
