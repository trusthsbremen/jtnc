package de.hsbremen.tc.tnc.message.m.serialize;

import java.util.List;

import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.ImData;
import de.hsbremen.tc.tnc.message.m.message.ImMessage;

public interface ImMessageContainer extends ImData{
	
	public List<ValidationException> getExceptions();
	
	public ImMessage getResult();
}
