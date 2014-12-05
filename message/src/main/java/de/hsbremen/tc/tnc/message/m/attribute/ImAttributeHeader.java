package de.hsbremen.tc.tnc.message.m.attribute;

import de.hsbremen.tc.tnc.message.m.ImData;

public interface ImAttributeHeader extends ImData {

	public long getVendorId();
	
	public long getAttributeType();
	
}
