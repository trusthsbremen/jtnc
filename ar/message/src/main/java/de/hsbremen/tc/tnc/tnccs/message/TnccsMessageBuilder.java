package de.hsbremen.tc.tnc.tnccs.message;

import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessage;

public interface TnccsMessageBuilder {

	public abstract TnccsMessage toMessage() throws ValidationException;

}