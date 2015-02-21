package org.ietf.nea.pa.attribute;

import org.ietf.nea.exception.RuleException;

import de.hsbremen.tc.tnc.message.m.attribute.ImAttributeValueBuilder;

public interface PaAttributeValueFactoryDefaultPasswordEnabledBuilder extends ImAttributeValueBuilder{
	
	public abstract PaAttributeValueFactoryDefaultPasswordEnabledBuilder setStatus(long status) throws RuleException;

}
