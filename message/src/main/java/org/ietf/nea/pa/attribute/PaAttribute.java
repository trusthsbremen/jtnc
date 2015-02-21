package org.ietf.nea.pa.attribute;

import de.hsbremen.tc.tnc.message.m.attribute.ImAttribute;
import de.hsbremen.tc.tnc.util.NotNull;

public class PaAttribute implements ImAttribute{


	private final PaAttributeHeader header;
	private final PaAttributeValue value;

	public PaAttribute(PaAttributeHeader header, PaAttributeValue value) {
		NotNull.check("Attribute header cannot be null.", header);
		
		NotNull.check("Attribute value cannot be null.", value);
		
		this.header = header;
		this.value = value;
	}
	
	@Override
	public PaAttributeHeader getHeader() {
		return this.header;
	}

	@Override
	public PaAttributeValue getValue() {
		return this.value;
	}

}
