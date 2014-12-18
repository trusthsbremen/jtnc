package de.hsbremen.tc.tnc.message.t.message;

import de.hsbremen.tc.tnc.message.t.TransportData;

public interface TransportMessageHeader extends TransportData{

	public long getIdentifier();
	
	/*
	 * Additional value not mandatory in the standards but it MUST be set to aid serialization
	 */
	public long getLength();
}
