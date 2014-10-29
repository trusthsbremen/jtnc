package org.ietf.nea.pa.attribute.util;

import org.ietf.nea.pa.message.PaMessageHeader;

public class PaAttributeValueErrorInformationInvalidParam extends AbstractPaAttributeValueErrorInformation{
  
    private final PaMessageHeader messageHeader; //variable length
    private final long offset;

	PaAttributeValueErrorInformationInvalidParam(final long length, final PaMessageHeader messageHeader, final long offset) {
		super(length);
		this.messageHeader = messageHeader;
		this.offset = offset;
	}

	/**
	 * @return the messageHeader
	 */
	public PaMessageHeader getMessageHeader() {
		return this.messageHeader;
	}

	/**
	 * @return the offset
	 */
	public long getOffset() {
		return this.offset;
	}
    
}
