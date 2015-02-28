package org.ietf.nea.pa.attribute;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.m.attribute.ImAttributeValueBuilder;

public interface PaAttributeValueForwardingEnabledBuilder extends ImAttributeValueBuilder{
	
	public abstract PaAttributeValueForwardingEnabledBuilder setStatus(long status) throws RuleException;

}
