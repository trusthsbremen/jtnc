package org.ietf.nea.pa.attribute;

import org.ietf.nea.pa.attribute.util.AttributeReference;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.m.attribute.ImAttributeValueBuilder;

public interface PaAttributeValueAttributeRequestBuilder extends ImAttributeValueBuilder{

	public abstract PaAttributeValueAttributeRequestBuilder addReferences(
			AttributeReference reference, AttributeReference... references)
			throws RuleException;
	
	
}
