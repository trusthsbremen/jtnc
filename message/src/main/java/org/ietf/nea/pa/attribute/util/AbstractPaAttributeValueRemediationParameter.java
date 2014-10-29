package org.ietf.nea.pa.attribute.util;


public abstract class AbstractPaAttributeValueRemediationParameter {
	
	private final long length;

	AbstractPaAttributeValueRemediationParameter(final long length) {
		this.length = length;
	}
	
	public long getLength(){
		return this.length;
	}
	
}
