package org.ietf.nea.pa.attribute.util;

import org.ietf.nea.pa.attribute.RawMessageHeader;

public class PaAttributeValueErrorInformationInvalidParam extends AbstractPaAttributeValueErrorInformation{
  
    private final long offset;

	PaAttributeValueErrorInformationInvalidParam(final long length, final RawMessageHeader messageHeader, final long offset) {
		super(length, messageHeader);
		this.offset = offset;
	}

	/**
	 * @return the offset
	 */
	public long getOffset() {
		return this.offset;
	}
    
}
