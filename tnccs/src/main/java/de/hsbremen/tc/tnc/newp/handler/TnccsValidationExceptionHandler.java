package de.hsbremen.tc.tnc.newp.handler;

import java.util.List;

import de.hsbremen.tc.tnc.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessage;

public interface TnccsValidationExceptionHandler {


	public abstract List<TnccsMessage> handle(List<ValidationException> exceptions);
	
}
