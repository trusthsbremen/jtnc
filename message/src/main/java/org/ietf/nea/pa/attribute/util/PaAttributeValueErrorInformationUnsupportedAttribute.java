package org.ietf.nea.pa.attribute.util;

import org.ietf.nea.pa.attribute.PaAttributeHeader;
import org.ietf.nea.pa.attribute.RawMessageHeader;

public class PaAttributeValueErrorInformationUnsupportedAttribute extends AbstractPaAttributeValueErrorInformation{
  
    private final PaAttributeHeader attributeHeader;

	PaAttributeValueErrorInformationUnsupportedAttribute(final long length, final RawMessageHeader messageHeader, final PaAttributeHeader attributeHeader) {
		super(length, messageHeader);
		this.attributeHeader = attributeHeader;
	}


	/**
	 * @return the attributeHeader
	 */
	public PaAttributeHeader getAttributeHeader() {
		return this.attributeHeader;
	}
    
}
