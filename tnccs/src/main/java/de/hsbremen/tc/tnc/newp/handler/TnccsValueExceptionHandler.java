package de.hsbremen.tc.tnc.newp.handler;

import java.util.List;

import de.hsbremen.tc.tnc.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessage;

public interface TnccsValueExceptionHandler {


	public abstract List<TnccsMessage> handle(List<ValidationException> exceptions);
	
}
