package org.ietf.nea.pa.attribute.util;


public abstract class AbstractPaAttributeValueErrorInformation {
	
	private final long length;

	AbstractPaAttributeValueErrorInformation(final long length) {
		this.length = length;
	}
	
	public long getLength(){
		return this.length;
	}
	
}
