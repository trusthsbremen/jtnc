package de.hsbremen.tc.tnc.tnccs.message.handler;

import java.util.List;

import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessage;

public interface TnccsValidationExceptionHandler {


	public abstract List<TnccsMessage> handle(List<ValidationException> exceptions);

	public abstract void dump(List<ValidationException> exceptions);
	
}
