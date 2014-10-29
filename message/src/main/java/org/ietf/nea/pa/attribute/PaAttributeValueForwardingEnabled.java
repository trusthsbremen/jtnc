package org.ietf.nea.pa.attribute;

import org.ietf.nea.pa.attribute.enums.PaAttributeForwardingStatusEnum;

public class PaAttributeValueForwardingEnabled extends AbstractPaAttributeValue {
	
	private final PaAttributeForwardingStatusEnum forwardingStatus; // 32 bit(s)
	
	PaAttributeValueForwardingEnabled(long length, PaAttributeForwardingStatusEnum forwardingStatus) {
		super(length);
		
		this.forwardingStatus = forwardingStatus;
	}

	/**
	 * @return the result
	 */
	public PaAttributeForwardingStatusEnum getForwardingStatus() {
		return this.forwardingStatus;
	}

	

	
}
