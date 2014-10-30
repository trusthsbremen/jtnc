package de.hsbremen.tc.tnc.tnccs.message;

import de.hsbremen.tc.tnc.tnccs.TnccsData;

public interface TnccsMessageHeader extends TnccsData{
	
	public long getVendorId();
	
	public long getMessageType();
	
}
