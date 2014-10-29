package org.ietf.nea.pa.attribute;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pa.attribute.util.PortFilterEntry;

import de.hsbremen.tc.tnc.m.attribute.ImAttributeValueBuilder;

public interface PaAttributeValuePortFilterBuilder extends ImAttributeValueBuilder{

	public abstract PaAttributeValuePortFilterBuilder addEntries(PortFilterEntry entry,
			PortFilterEntry... entries) throws RuleException;
	
}
