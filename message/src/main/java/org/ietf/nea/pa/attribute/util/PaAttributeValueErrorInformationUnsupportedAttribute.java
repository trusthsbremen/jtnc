package org.ietf.nea.pa.attribute.util;

import org.ietf.nea.pa.attribute.PaAttributeHeader;
import org.ietf.nea.pa.message.PaMessageHeader;

public class PaAttributeValueErrorInformationUnsupportedAttribute extends AbstractPaAttributeValueErrorInformation{
  
    private final PaMessageHeader messageHeader; //variable length
    private final PaAttributeHeader attributeHeader;

	PaAttributeValueErrorInformationUnsupportedAttribute(final long length, final PaMessageHeader messageHeader, final PaAttributeHeader attributeHeader) {
		super(length);
		this.messageHeader = messageHeader;
		this.attributeHeader = attributeHeader;
	}

	/**
	 * @return the messageHeader
	 */
	public PaMessageHeader getMessageHeader() {
		return this.messageHeader;
	}

	/**
	 * @return the attributeHeader
	 */
	public PaAttributeHeader getAttributeHeader() {
		return this.attributeHeader;
	}
    
}
