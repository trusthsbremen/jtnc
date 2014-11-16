package org.ietf.nea.pa.attribute.util;

import org.ietf.nea.pa.attribute.RawMessageHeader;

import de.hsbremen.tc.tnc.m.attribute.ImAttributeSubValue;


public abstract class AbstractPaAttributeValueErrorInformation implements ImAttributeSubValue{
	
	private final long length;
	private final RawMessageHeader messageHeader; //variable length
	
	
	AbstractPaAttributeValueErrorInformation(final long length, RawMessageHeader messageHeader) {
		this.length = length;
		this.messageHeader = messageHeader;
	}
	
	public long getLength(){
		return this.length;
	}

	/**
	 * @return the messageHeader
	 */
	public RawMessageHeader getMessageHeader() {
		return this.messageHeader;
	}
	
	
	
}
