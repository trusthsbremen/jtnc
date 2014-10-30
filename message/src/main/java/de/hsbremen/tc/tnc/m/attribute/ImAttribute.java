package de.hsbremen.tc.tnc.m.attribute;

import de.hsbremen.tc.tnc.m.ImData;


public interface ImAttribute extends ImData{
	public ImAttributeHeader getHeader();
	
	public ImAttributeValue getValue();
	
}
