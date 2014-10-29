package org.ietf.nea.pa.attribute;

import java.util.Collections;
import java.util.List;

import org.ietf.nea.pa.attribute.util.AttributeReference;

public class PaAttributeValueAttributeRequest extends AbstractPaAttributeValue {
	
	private final List<AttributeReference> references; // 24 bit(s) key, 32 bit(s) value must have at minimum one entry
	
	PaAttributeValueAttributeRequest(long length, List<AttributeReference> references) {
		super(length);
		
		this.references = references;
	}

	/**
	 * @return the references
	 */
	public List<AttributeReference> getReferences() {
		return Collections.unmodifiableList(this.references);
	}

	
}
