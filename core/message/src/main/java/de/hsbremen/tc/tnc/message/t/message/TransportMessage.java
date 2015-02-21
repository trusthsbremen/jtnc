package de.hsbremen.tc.tnc.message.t.message;

import de.hsbremen.tc.tnc.message.t.TransportData;
import de.hsbremen.tc.tnc.message.t.value.TransportMessageValue;



public interface TransportMessage extends TransportData{
	
	public TransportMessageHeader getHeader();
	public TransportMessageValue getValue();
	
}
