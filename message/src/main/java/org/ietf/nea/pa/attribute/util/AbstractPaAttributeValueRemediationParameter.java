package org.ietf.nea.pa.attribute.util;

import de.hsbremen.tc.tnc.message.m.attribute.ImAttributeSubValue;


public abstract class AbstractPaAttributeValueRemediationParameter implements ImAttributeSubValue {
	
	private final long length;

	AbstractPaAttributeValueRemediationParameter(final long length) {
		this.length = length;
	}
	
	public long getLength(){
		return this.length;
	}
	
}
