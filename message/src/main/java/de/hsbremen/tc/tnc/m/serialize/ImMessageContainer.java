package de.hsbremen.tc.tnc.m.serialize;

import java.util.List;

import de.hsbremen.tc.tnc.exception.ValidationException;
import de.hsbremen.tc.tnc.m.ImData;
import de.hsbremen.tc.tnc.m.message.ImMessage;

public interface ImMessageContainer extends ImData{
	
	public List<ValidationException> getExceptions();
	
	public ImMessage getResult();
}
