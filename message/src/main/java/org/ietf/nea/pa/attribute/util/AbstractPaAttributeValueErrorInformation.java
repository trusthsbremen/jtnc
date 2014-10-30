package org.ietf.nea.pa.attribute.util;

import de.hsbremen.tc.tnc.m.attribute.ImAttributeSubValue;


public abstract class AbstractPaAttributeValueErrorInformation implements ImAttributeSubValue{
	
	private final long length;

	AbstractPaAttributeValueErrorInformation(final long length) {
		this.length = length;
	}
	
	public long getLength(){
		return this.length;
	}
	
}
