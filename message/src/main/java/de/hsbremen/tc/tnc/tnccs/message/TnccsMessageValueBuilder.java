package de.hsbremen.tc.tnc.tnccs.message;

import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;

public interface TnccsMessageValueBuilder {

	public abstract TnccsMessageValue toValue() throws ValidationException;

	public abstract TnccsMessageValueBuilder clear();
}
