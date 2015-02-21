package de.hsbremen.tc.tnc.message.m.attribute;

import de.hsbremen.tc.tnc.message.m.ImData;


public interface ImAttribute extends ImData{
	public ImAttributeHeader getHeader();
	
	public ImAttributeValue getValue();
	
}
