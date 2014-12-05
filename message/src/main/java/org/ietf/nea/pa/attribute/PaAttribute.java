package org.ietf.nea.pa.attribute;

import de.hsbremen.tc.tnc.message.m.attribute.ImAttribute;

public class PaAttribute implements ImAttribute{


	private final PaAttributeHeader header;
	private final PaAttributeValue value;

	public PaAttribute(PaAttributeHeader header, PaAttributeValue value) {
		if(header == null){
			throw new NullPointerException("Attribute header cannot be null.");
		}
		if(value == null){
			throw new NullPointerException("Attribute value cannot be null.");
		}
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
