package de.hsbremen.tc.tnc.m.message;

import java.util.List;

import de.hsbremen.tc.tnc.exception.ValidationException;
import de.hsbremen.tc.tnc.m.ImData;

public interface ImMessageContainer extends ImData{
	
	public List<ValidationException> getExceptions();
	
	public ImMessage getResult();
}
