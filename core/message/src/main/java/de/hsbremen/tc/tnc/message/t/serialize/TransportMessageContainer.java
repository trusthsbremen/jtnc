package de.hsbremen.tc.tnc.message.t.serialize;

import java.util.List;

import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.t.TransportData;
import de.hsbremen.tc.tnc.message.t.message.TransportMessage;

public interface TransportMessageContainer extends TransportData{
	
	public List<ValidationException> getExceptions();
	
	public TransportMessage getResult();
}
